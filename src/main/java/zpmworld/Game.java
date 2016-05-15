package zpmworld;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.File;
import java.util.List;
import javax.swing.Timer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Game implements KeyListener{
	private State state;
	private Stage stage;
	private Player Jaffa;
	private Player Oneill;
	private ActionUnit Replicator;
	private boolean pause;
	private Graphic graphic;
	private Status status;

	private static final int GAMESPEED = 90; //update millisec
	private static final int FPS = 16; //update millisec

    private Map<Class,Character> classCharacterMap;

	Game(Graphic graphic, Status status) throws Exception {
		state = State.GAME;
		pause = false;
		this.graphic = graphic;
		this.status = status;
        classCharacterMap = initClassMap();
	}

    private Map<Class,Character> initClassMap(){
        Map<Class,Character> retMap = new HashMap<Class, Character>();
        retMap.put(Abyss.class,'a');
        retMap.put(Road.class,'r');
        retMap.put(Wall.class,'w');
        retMap.put(PortalWall.class,'p');
        retMap.put(Scale.class,'s');
        retMap.put(Gate.class,'g');
        return retMap;
    }

	public void registerDrawableField(Drawable fieldDrawable){
		graphic.registerDrawableField(fieldDrawable);
	}

	public void registerDrawableUnit(Drawable unitDrawable){
		graphic.registerDrawableUnit(unitDrawable);
	}

	public void deleteDrawableField(Field field){
		graphic.deleteDrawableField(field);
	}

	public void setReplicator(Replicator replicator){
		this.Replicator = replicator;
	}

	public State getState(){
		return state;
	}

	public void setState(State state){
		this.state = state;
	}

	public void update(){
		if(!pause){
			stage.update();
			stage.collectUnits();
			graphic.update();
			if(Replicator != null && Replicator.isDead()){
				stage.createReplicator();
			}
		}
	}

	public void loadGame(){}
	public void saveGame(){
        List<Field> inGameFields = null;
        List<Unit> inGameUnits = null;
        if(stage != null) {
            inGameFields = stage.getFields();
            inGameUnits = stage.getUnits();
        } else {
            return;
        }

        try {
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =
                    dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // root element Stage
            Element rootElement = doc.createElement("Stage");
            doc.appendChild(rootElement);

            Element rows = doc.createElement("rows");
            rootElement.appendChild(rows);

            //  rows
            int i = 0;
            int j = 0;
            while(stage.getField(new Point(j,i)) != null){
                StringBuilder rowBuilder = new StringBuilder();
                while(stage.getField(new Point(j,i)) != null){
                    Field field = stage.getField(new Point(j,i));
                    char classChar = classCharacterMap.get(field.getClass());
                    rowBuilder.append(classChar);

                    j++;
                }
                //sor xml
                Element rowElement = doc.createElement("row");
                Attr attrType = doc.createAttribute("id");
                attrType.setValue(String.valueOf(i));

                rowElement.setAttributeNode(attrType);
                rowElement.appendChild(doc.createTextNode(rowBuilder.toString()));
                rows.appendChild(rowElement);

                j = 0;
                i++;
            }

            // units
            Element units = doc.createElement("units");
            rootElement.appendChild(units);

            for(Unit unit : inGameUnits){
                if(unit.getXmlElement(doc) != null) {
                    units.appendChild(unit.getXmlElement(doc));
                }
            }

            // field connection, portalwall colors

            Element field_properties = doc.createElement("field_properties");
            rootElement.appendChild(field_properties);

            for(Field field : inGameFields){
                if(field.getXmlElement(doc) != null) {
                    field_properties.appendChild(field.getXmlElement(doc));
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();
            Transformer transformer =
                    transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result =
                    new StreamResult(new File("saveGame.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void console(){

		BufferedReader instream = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String inputLine = instream.readLine();
				if (inputLine == null) break;
				StringTokenizer tokenizer = new StringTokenizer(inputLine, " -");
				String command = tokenizer.nextToken();

				if ("newGame".startsWith(command)) {
					if (tokenizer.hasMoreTokens()){
						newGame(new File(readString(tokenizer)));
					} else {
						newGame();
					}
				} else if ("update".startsWith(command)) {
					if (!tokenizer.hasMoreTokens()){
						update();
						//kimenet kiíratása
						List<String> changes = stage.getLog();
						if(changes != null) {
							for (String str : changes) {
								System.out.println(str);
							}
							//break;        //az update után kilép a program
						} else {
							System.out.println("");		//nem történt változás
						}
					} else {
						String updateState = readString(tokenizer);
						if("pause".startsWith(updateState)){
							pause();
						} else if ("resume".startsWith(updateState)) {
							resume();
						} else {
							throw new Exception("Az update automatizálása a pause és resume paraméterekkel állítható!");
						}
					}
				} else if ("loadGame".startsWith(command)) {
					//// TODO: 2016. 04. 23.
				} else if ("saveGame".startsWith(command)) {
					//// TODO: 2016. 04. 23.
				} else if ("exitGame".startsWith(command)) {
					break;
				//Cselekvések
				} else if ("move".startsWith(command)) {
					String executor = readString(tokenizer);
					String cmd = "move " + executor;
					control(cmd);

				} else if ("turn".startsWith(command)) {
					String executor = readString(tokenizer);
					String dir = readString(tokenizer);
					String cmd = "turn " + executor + " " + dir;
					control(cmd);
				} else if ("grab".startsWith(command)) {
					String executor = readString(tokenizer);
					String cmd = "grab " + executor;
					control(cmd);
				} else if ("drop".startsWith(command)) {
					String executor = readString(tokenizer);
					String cmd = "drop " + executor;
					control(cmd);
				} else if ("shoot".startsWith(command)) {
					String executor = readString(tokenizer);
					String color = readString(tokenizer);
					String cmd = "shoot " + executor + " " + color;
					control(cmd);
				} else if ("getUnit".startsWith(command)) {
					String executor = readString(tokenizer);
					if("oneill".startsWith(executor)){
						if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
						System.out.println(Oneill.toString());
					} else if ("jaffa".startsWith(executor)) {
						if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
						System.out.println(Jaffa.toString());
					} else if ("replicator".startsWith(executor)) {
						if(Replicator == null) throw new Exception("Nincs Replicator a pályán");
						System.out.println(Replicator.toString());
					} else {
						throw new Exception("Hibás lekérdezendõ egységazonosító!");
					}
				} else if ("getZPM".startsWith(command)) {
					if(tokenizer.hasMoreTokens()){
						String executor = readString(tokenizer);
						if("oneill".startsWith(executor)){
							if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
							System.out.println(((Player)Oneill).getCollectedZPM());
						} else if ("jaffa".startsWith(executor)) {
							if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
							System.out.println(((Player)Jaffa).getCollectedZPM());
						} else {
							throw new Exception("Hibás lekérdezendõ egységazonosító!");
						}
					} else {
						stage.getAllZPM();
					}
				} else if ("getField".startsWith(command)) {
					int posX = Integer.parseInt(readString(tokenizer));
					int posY = Integer.parseInt(readString(tokenizer));
					Field target = stage.getField(new Point(posX, posY));
					if(target == null) throw new Exception("Az adott pozíción nincsen mezõ!");
					System.out.println(target.toString());
				} else if ("getPortal".startsWith(command)) {
					System.out.println(stage.getPortal().toString());
				} else if ("listBoxes".startsWith(command)) {
					List<Unit> boxes = stage.listBoxes();
					for(Unit unit : boxes){
						System.out.println(unit.toString());
					}
				} else if ("listZPMs".startsWith(command)) {
					List<ZPM> ZPMs = stage.listZPM();
					for(ZPM zpm : ZPMs){
						System.out.println(zpm.toString());
					}
				} else if ("setReplicatorDir".startsWith(command)) {
					String direction = readString(tokenizer);
					Direction dir;

					if("north".startsWith(direction)){
						dir = Direction.NORTH;
					} else if ("west".startsWith(direction)) {
						dir = Direction.WEST;
					} else if ("east".startsWith(direction)) {
						dir = Direction.EAST;
					} else if ("sout".startsWith(direction)) {
						dir = Direction.SOUTH;
					} else {
						throw new Exception("Hibás fordulási irány!");
					}

					setReplicatorDir(false, dir);
				} else if ("setUnitPos".startsWith(command)) {
					String firstparam = readString(tokenizer);
					if("oneill".startsWith(firstparam)){
						String targetX;
						String targetY;
						Field targetField = null;
						try {
							targetX = readString(tokenizer);
							targetY = readString(tokenizer);
							int X = Integer.parseInt(targetX);
							int Y = Integer.parseInt(targetY);
							targetField = stage.getField(new Point(X,Y));
						} catch (Exception e){
							throw new Exception("Hibásan megadott célmezõ paraméter!");
						}
						if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
						stage.setUnitPos(Oneill, targetField);
					} else if ("jaffa".startsWith(firstparam)) {
						String targetX;
						String targetY;
						Field targetField = null;
						try {
							targetX = readString(tokenizer);
							targetY = readString(tokenizer);
							int X = Integer.parseInt(targetX);
							int Y = Integer.parseInt(targetY);
							targetField = stage.getField(new Point(X,Y));
						} catch (Exception e){
							throw new Exception("Hibásan megadott célmezõ paraméter!");
						}
						if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
						stage.setUnitPos(Jaffa, targetField);
					} else if ("replicator".startsWith(firstparam)) {
						String targetX;
						String targetY;
						Field targetField = null;
						try {
							targetX = readString(tokenizer);
							targetY = readString(tokenizer);
							int X = Integer.parseInt(targetX);
							int Y = Integer.parseInt(targetY);
							targetField = stage.getField(new Point(X,Y));
						} catch (Exception e){
							throw new Exception("Hibásan megadott célmezõ paraméter!");
						}
						if(Replicator == null) throw new Exception("Nincs Replicator a pályán");
						stage.setUnitPos(Replicator, targetField);
					} else {
						String originX;
						String originY;
						Field originField;
						try {
							originX = firstparam;
							originY = readString(tokenizer);
							int X = Integer.parseInt(originX);
							int Y = Integer.parseInt(originY);
							originField = stage.getField(new Point(X,Y));
						} catch (Exception e){
							throw new Exception("Hibásan megadott indulási mezõ paraméter!");
						}

						String targetX;
						String targetY;
						Field targetField;
						try {
							targetX = readString(tokenizer);
							targetY = readString(tokenizer);
							int X = Integer.parseInt(targetX);
							int Y = Integer.parseInt(targetY);
							targetField = stage.getField(new Point(X,Y));
						} catch (Exception e){
							throw new Exception("Hibásan megadott célmezõ paraméter!");
						}
						stage.setUnitPos(originField,targetField);
					}
				} else if ("killUnit".startsWith(command)) {
					String executor = readString(tokenizer);
					if("oneill".startsWith(executor)){
						if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
						Oneill.kill();
					} else if ("jaffa".startsWith(executor)) {
						if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
						Jaffa.kill();
					} else if ("replicator".startsWith(executor)) {
						if(Replicator == null) throw new Exception("Nincs Replicator a pályán");
						Replicator.kill();
					} else {
						int posX = Integer.parseInt(readString(tokenizer));
						int posY = Integer.parseInt(readString(tokenizer));
						stage.killUnit(stage.getField(new Point(posX, posY)));
					}
				} else if ("addBox".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs üres mezõ!");
					Box newBox = new Box(target);
					target.addUnit(newBox);
					stage.addUnit(newBox);
				} else if ("addZPM".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs üres mezõ!");
					ZPM newZPM = new ZPM(target);
					target.addUnit(newZPM);
					stage.addUnit(newZPM);
				} else if ("addReplicator".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs üres mezõ!");
					Replicator newReplicator = new Replicator(this, Direction.NORTH, target);
					target.addUnit(newReplicator);
					stage.addUnit(newReplicator);
				} else if ("talkativeStage".startsWith(command)) {
					String talkState = readString(tokenizer);
					if("on".startsWith(talkState)){
						talkativeStage();
					} else if ("off".startsWith(talkState)) {
						muteStage();
					} else {
						throw new Exception("A Stage beszédessége csak on illetve off paraméterrel állítható!");
					}
				} else {
					throw new Exception("Hibás parancs! (" + inputLine + ")");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				//break;
			}
		}
	}

	private String readString(StringTokenizer tokenizer) throws Exception{
		if (tokenizer.hasMoreElements()) {
			return tokenizer.nextToken();
		} else {
			throw new Exception("Keves parameter!");
		}
	}

	public void control(String cmd) throws Exception{
		/*? Konverzió a külvilág és a játék belsõ mûködése között: a 
		felhasználói bemeneti eseményeknek megfelelõen helyesen beállítja a Player 
		következõ update()­ben végrehajtandó cselekedetét. A cmd paraméterben kapott 
		információ alapján tudja, hogy mit kell beállítani. A mûködés egy egyszerû switch 
		case szerkezet és a parancs darabolás (parsolása). */

		try {
			StringTokenizer tokenizer = new StringTokenizer(cmd, " -");
			String command = tokenizer.nextToken();

			if("move".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
					Oneill.move();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
					Jaffa.move();
				} else if ("replicator".startsWith(executor)) {
					if(Replicator == null) throw new Exception("Nincs Replicator a pályán");
					Replicator.move();
				} else {
					throw new Exception("Hibás végrehajtó a move cselekvésnél!");
				}
			} else if ("turn".startsWith(command)){
				String executor = readString(tokenizer);
				String direction = readString(tokenizer);
				Direction dir;

				if("north".startsWith(direction)){
					dir = Direction.NORTH;
				} else if ("west".startsWith(direction)) {
					dir = Direction.WEST;
				} else if ("east".startsWith(direction)) {
					dir = Direction.EAST;
				} else if ("south".startsWith(direction)) {
					dir = Direction.SOUTH;
				} else {
					throw new Exception("Hibás fordulási irány!");
				}

				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
					Oneill.turn(dir);
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
					Jaffa.turn(dir);
				} else if ("replicator".startsWith(executor)) {
					if(Replicator == null) throw new Exception("Nincs Replicator a pályán");
					Replicator.turn(dir);
				} else {
					throw new Exception("Hibás végrehajtó a turn cselekvésnél!");
				}
			} else if ("grab".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
					Oneill.grab();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
					Jaffa.grab();
				} else {
					throw new Exception("Hibás végrehajtó a grab cselekvésnél!");
				}
			} else if ("drop".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
					Oneill.drop();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
					Jaffa.drop();
				} else {
					throw new Exception("Hibás végrehajtó a drop cselekvésnél!");
				}
			} else if ("shoot".startsWith(command)){
				String executor = readString(tokenizer);
				String colorStr = readString(tokenizer);
				Color color;

				if("blue".startsWith(colorStr)){
					color = Color.BLUE;
				} else if ("yellow".startsWith(colorStr)) {
					color = Color.YELLOW;
				} else if ("red".startsWith(colorStr)) {
					color = Color.RED;
				} else if ("green".startsWith(colorStr)) {
					color = Color.GREEN;
				} else {
					throw new Exception("Hibás lövedék szín!");
				}

				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a pályán");
					Oneill.shoot(color);
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a pályán");
					Jaffa.shoot(color);
				} else {
					throw new Exception("Hibás végrehajtó a shoot cselekvésnél!");
				}
			} else {
				throw new Exception("Hibás végrehajtandó parancsmegnevezés!");
			}

		} catch (Exception e){
			throw new Exception(e);
		}

	}

	String command(String cmd){
		/*Lekérdezõ parancsokat (pl. getField, getUnit), (a 
		külvilági) játékostól függõ manipulációs parancsokat (lényegében amit a control tud), 
		valamint játékostól független manipulációs parancsokat (pl. killUnit, addBox) 
		hajthatunk végre vele. A kapott paraméter maga a parancs. Visszatér a kimenettel (ha van neki).
		 A mûködés egy egyszerû switch case szerkezet, illetvet a parancs 
		darabolása. (A lekérdezések kivétel nélkül toString felüldefiniálásával történnek a 
		megfelelõ objektum elkérése után, pl. Stage­tõl)*/

		return null;
	}

	public void talkativeStage(){
		stage.logOn();
	}

	public void muteStage(){
		stage.logOff();
	}

	public void pause(){
		if (state != State.PAUSE) {
			pause = true;
			state = State.PAUSE;
		}
		else {
			state = State.GAME;
			pause = false;
		}
	}

	public void resume(){
		pause = false;
	}

	public void lose(){

        if(state == State.GAME) {
            state = State.LOSE;
            status.endgame(false);
        }
	}

	public boolean isPause(){
		return pause;
	}

	public void setJaffa(Player Jaffa){
		this.Jaffa = Jaffa;
	}

	public void setOneill(Player Oneill){
		this.Oneill = Oneill;
	}

	public void setReplicatorDir(boolean random, Direction dir) throws Exception{
		if(Replicator == null) throw new Exception("Nincs replicator a pályán!");
		if(random){
			int randomDir = new Random().nextInt(4) + 1;
			switch (randomDir){
				case 1:
					Replicator.turn(Direction.NORTH);
					break;
				case 2:
					Replicator.turn(Direction.WEST);
					break;
				case 3:
					Replicator.turn(Direction.EAST);
					break;
				case 4:
					Replicator.turn(Direction.SOUTH);
					break;
				default:
					break;
			}
		} else {
			Replicator.turn(dir);
		}
	}

    public boolean killEnabledToEnemy(Player player) {
        if(player.getName() == "O'neill"){
            return Jaffa.getEnabledToKill();
        } else {
            return Oneill.getEnabledToKill();
        }
    }


    public boolean enableToKill(Player player) {
        if(Math.ceil(stage.getOriginalZPMNumber() * 0.4) <= player.getCollectedZPM()){     //ha az eredeti zpm-szám legalább 40%-át begyûjtötte már, akkor ölhet
            return true;
        } else {
            return false;
        }
    }

    public boolean ONeillWon() {
        return Oneill.getCollectedZPM() >= Jaffa.getCollectedZPM();
    }

	public void win(){
        if(state == State.GAME) {
            state = State.WIN;
            status.endgame(true);
        }
	}
	public void addUnit(Unit unit){
		stage.addUnit(unit);
	}
    public void createBullet(Bullet bullet){
        stage.createBullet(bullet);
    }
	public void createZPM(){
		stage.createZPM();
	}
	public int getAllZPM(){
		return stage.getAllZPM();
	}
	public void replaceField(Field field){
		stage.replaceField(field);
	}
	public void newGame(File file) throws Exception{
        state = State.GAME;
		try {
			graphic.clear();
			status.clear();
			if(stage == null)
				setLoop();
			this.stage = new Stage(file, this);
		} catch (Exception e){
			throw new Exception(e);
		}
	}
	public void newGame() throws Exception{
		try {
			graphic.clear();
			status.clear();
			if(stage == null)
				setLoop();
			this.stage = new Stage(new File("testMap.xml"), this);
		} catch (Exception e){
			throw new Exception(e);
		}
	}

	private void setLoop(){
		setState(State.GAME);
		Timer updateTimer = new Timer(GAMESPEED, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getState() != State.PAUSE)
					update();
					status.update(getAllZPM(), Oneill.getCollectedZPM(), Jaffa.getCollectedZPM(), Oneill.getBox(), Jaffa.getBox(), Oneill.getEnabledToKill(), Jaffa.getEnabledToKill());
			}
		});
		Timer paintTimer = new Timer(FPS, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(getState() != State.PAUSE) {
					graphic.repaint();
				}
			}
		});
		updateTimer.setInitialDelay(100);
		paintTimer.setInitialDelay(200);
		updateTimer.start();
		paintTimer.start();
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W){
			if(Oneill.getCurrentDirection() == Direction.NORTH)
				Oneill.move();
			else
				Oneill.turn(Direction.NORTH);
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			if(Oneill.getCurrentDirection() == Direction.WEST)
				Oneill.move();
			else
				Oneill.turn(Direction.WEST);
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			if(Oneill.getCurrentDirection() == Direction.EAST)
				Oneill.move();
			else
				Oneill.turn(Direction.EAST);
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			if(Oneill.getCurrentDirection() == Direction.SOUTH)
				Oneill.move();
			else
				Oneill.turn(Direction.SOUTH);
		}
		if(e.getKeyCode() == KeyEvent.VK_R){
			if(Oneill.getBox() == null)
				Oneill.grab();
			else
				Oneill.drop();
		}

		if(e.getKeyCode() == KeyEvent.VK_E){
			Oneill.shoot(Color.BLUE);
		}
		if(e.getKeyCode() == KeyEvent.VK_Q){
			Oneill.shoot(Color.YELLOW);
		}

		if(e.getKeyCode() == KeyEvent.VK_UP){
			if(Jaffa.getCurrentDirection() == Direction.NORTH)
				Jaffa.move();
			else
				Jaffa.turn(Direction.NORTH);
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if(Jaffa.getCurrentDirection() == Direction.WEST)
				Jaffa.move();
			else
				Jaffa.turn(Direction.WEST);
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			if(Jaffa.getCurrentDirection() == Direction.EAST)
				Jaffa.move();
			else
				Jaffa.turn(Direction.EAST);
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if(Jaffa.getCurrentDirection() == Direction.SOUTH)
				Jaffa.move();
			else
				Jaffa.turn(Direction.SOUTH);
		}
		if(e.getKeyCode() == KeyEvent.VK_B){
			if(Jaffa.getBox() == null)
				Jaffa.grab();
			else
				Jaffa.drop();
		}

		if(e.getKeyCode() == KeyEvent.VK_N){
			Jaffa.shoot(Color.GREEN);
		}
		if(e.getKeyCode() == KeyEvent.VK_M){
			Jaffa.shoot(Color.RED);
		}

		if(e.getKeyCode() == KeyEvent.VK_V){
			Graphics2D g = (Graphics2D) graphic.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			update();
		}
	}

	public void keyReleased(KeyEvent e) {

	}
}