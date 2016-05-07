package zpmworld;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.List;
import java.io.File;
import java.util.Random;

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

	/*public static void main(String[] args){
		try {
			Graphic graphic = new Graphic(800,600);
			Game game = new Game(graphic);
			System.out.println("�dv�z�llek a ZPM vil�g nev� j�t�kban!");
			//later check args and run appropiate command
			//File argFile = new File("t1.xml");
			//game.newGame(argFile);
			game.console();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	Game(Graphic graphic, Status status) throws Exception {
		state = State.GAME;
		pause = false;
		this.graphic = graphic;
		this.status = status;
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

	public void deleteDrawableUnit(Unit unit){
		graphic.deleteDrawableUnit(unit);
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
	public void saveGame(){}

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
						//kimenet ki�rat�sa
						List<String> changes = stage.getLog();
						if(changes != null) {
							for (String str : changes) {
								System.out.println(str);
							}
							//break;        //az update ut�n kil�p a program
						} else {
							System.out.println("");		//nem t�rt�nt v�ltoz�s
						}
					} else {
						String updateState = readString(tokenizer);
						if("pause".startsWith(updateState)){
							pause();
						} else if ("resume".startsWith(updateState)) {
							resume();
						} else {
							throw new Exception("Az update automatiz�l�sa a pause �s resume param�terekkel �ll�that�!");
						}
					}
				} else if ("loadGame".startsWith(command)) {
					//// TODO: 2016. 04. 23.
				} else if ("saveGame".startsWith(command)) {
					//// TODO: 2016. 04. 23.
				} else if ("exitGame".startsWith(command)) {
					break;
				//Cselekv�sek
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
						if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
						System.out.println(Oneill.toString());
					} else if ("jaffa".startsWith(executor)) {
						if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
						System.out.println(Jaffa.toString());
					} else if ("replicator".startsWith(executor)) {
						if(Replicator == null) throw new Exception("Nincs Replicator a p�ly�n");
						System.out.println(Replicator.toString());
					} else {
						throw new Exception("Hib�s lek�rdezend� egys�gazonos�t�!");
					}
				} else if ("getZPM".startsWith(command)) {
					if(tokenizer.hasMoreTokens()){
						String executor = readString(tokenizer);
						if("oneill".startsWith(executor)){
							if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
							System.out.println(((Player)Oneill).getCollectedZPM());
						} else if ("jaffa".startsWith(executor)) {
							if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
							System.out.println(((Player)Jaffa).getCollectedZPM());
						} else {
							throw new Exception("Hib�s lek�rdezend� egys�gazonos�t�!");
						}
					} else {
						stage.getZPM();
					}
				} else if ("getField".startsWith(command)) {
					int posX = Integer.parseInt(readString(tokenizer));
					int posY = Integer.parseInt(readString(tokenizer));
					Field target = stage.getField(new Point(posX, posY));
					if(target == null) throw new Exception("Az adott poz�ci�n nincsen mez�!");
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
						throw new Exception("Hib�s fordul�si ir�ny!");
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
							throw new Exception("Hib�san megadott c�lmez� param�ter!");
						}
						if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
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
							throw new Exception("Hib�san megadott c�lmez� param�ter!");
						}
						if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
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
							throw new Exception("Hib�san megadott c�lmez� param�ter!");
						}
						if(Replicator == null) throw new Exception("Nincs Replicator a p�ly�n");
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
							throw new Exception("Hib�san megadott indul�si mez� param�ter!");
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
							throw new Exception("Hib�san megadott c�lmez� param�ter!");
						}
						stage.setUnitPos(originField,targetField);
					}
				} else if ("killUnit".startsWith(command)) {
					String executor = readString(tokenizer);
					if("oneill".startsWith(executor)){
						if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
						Oneill.kill();
					} else if ("jaffa".startsWith(executor)) {
						if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
						Jaffa.kill();
					} else if ("replicator".startsWith(executor)) {
						if(Replicator == null) throw new Exception("Nincs Replicator a p�ly�n");
						Replicator.kill();
					} else {
						int posX = Integer.parseInt(readString(tokenizer));
						int posY = Integer.parseInt(readString(tokenizer));
						stage.killUnit(stage.getField(new Point(posX, posY)));
					}
				} else if ("addBox".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs �res mez�!");
					Box newBox = new Box(target);
					target.addUnit(newBox);
					stage.addUnit(newBox);
				} else if ("addZPM".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs �res mez�!");
					ZPM newZPM = new ZPM(target);
					target.addUnit(newZPM);
					stage.addUnit(newZPM);
				} else if ("addReplicator".startsWith(command)) {
					Road target = (Road)stage.getEmptyRoad();
					if(target == null) throw new Exception("Nincs �res mez�!");
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
						throw new Exception("A Stage besz�dess�ge csak on illetve off param�terrel �ll�that�!");
					}
				} else {
					throw new Exception("Hib�s parancs! (" + inputLine + ")");
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
		/*? Konverzi�a�k�lvil�g��s�a�j�t�k�bels��m�k�d�se�k�z�tt:�a�
		felhaszn�l�i�bemeneti�esem�nyeknek�megfelel�en�helyesen�be�ll�tja�a�Player�
		k�vetkez��update()�ben�v�grehajtand�cselekedet�t.�A�cmd�param�terben�kapott�
		inform�ci�alapj�n�tudja,�hogy�mit�kell�be�ll�tani.�A�m�k�d�s�egy�egyszer��switch�
		case�szerkezet��s�a�parancs�darabol�s�(parsol�sa).�*/

		try {
			StringTokenizer tokenizer = new StringTokenizer(cmd, " -");
			String command = tokenizer.nextToken();

			if("move".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
					Oneill.move();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
					Jaffa.move();
				} else if ("replicator".startsWith(executor)) {
					if(Replicator == null) throw new Exception("Nincs Replicator a p�ly�n");
					Replicator.move();
				} else {
					throw new Exception("Hib�s v�grehajt� a move cselekv�sn�l!");
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
					throw new Exception("Hib�s fordul�si ir�ny!");
				}

				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
					Oneill.turn(dir);
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
					Jaffa.turn(dir);
				} else if ("replicator".startsWith(executor)) {
					if(Replicator == null) throw new Exception("Nincs Replicator a p�ly�n");
					Replicator.turn(dir);
				} else {
					throw new Exception("Hib�s v�grehajt� a turn cselekv�sn�l!");
				}
			} else if ("grab".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
					Oneill.grab();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
					Jaffa.grab();
				} else {
					throw new Exception("Hib�s v�grehajt� a grab cselekv�sn�l!");
				}
			} else if ("drop".startsWith(command)){
				String executor = readString(tokenizer);
				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
					Oneill.drop();
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
					Jaffa.drop();
				} else {
					throw new Exception("Hib�s v�grehajt� a drop cselekv�sn�l!");
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
					throw new Exception("Hib�s l�ved�k sz�n!");
				}

				if("oneill".startsWith(executor)){
					if(Oneill == null) throw new Exception("Nincs Oneill a p�ly�n");
					Oneill.shoot(color);
				} else if ("jaffa".startsWith(executor)) {
					if(Jaffa == null) throw new Exception("Nincs Jaffa a p�ly�n");
					Jaffa.shoot(color);
				} else {
					throw new Exception("Hib�s v�grehajt� a shoot cselekv�sn�l!");
				}
			} else {
				throw new Exception("Hib�s v�grehajtand� parancsmegnevez�s!");
			}

		} catch (Exception e){
			throw new Exception(e);
		}

	}

	String command(String cmd){
		/*Lek�rdez��parancsokat�(pl.�getField,�getUnit),�(a�
		k�lvil�gi)�j�t�kost�l�f�gg��manipul�ci�s�parancsokat�(l�nyeg�ben�amit�a�control�tud),�
		valamint�j�t�kost�l�f�ggetlen�manipul�ci�s�parancsokat�(pl.�killUnit,�addBox)�
		hajthatunk�v�gre�vele.�A�kapott�param�ter�maga�a�parancs.�Visszat�r�a�kimenettel�(ha�van�neki).
		�A�m�k�d�s�egy�egyszer��switch�case�szerkezet,�illetvet�a�parancs�
		darabol�sa.�(A�lek�rdez�sek�kiv�tel�n�lk�l�toString�fel�ldefini�l�s�val�t�rt�nnek�a�
		megfelel��objektum�elk�r�se�ut�n,�pl.�Stage�t�l)*/

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

		state = State.LOSE;
		status.endgame(false);
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
		if(Replicator == null) throw new Exception("Nincs replicator a p�ly�n!");
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

    public boolean ONeillWon() {
        return Oneill.getCollectedZPM() >= Jaffa.getCollectedZPM();
    }

	public void win(){
		state = State.WIN;
		status.endgame(true);
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
		return stage.getZPM();
	}
	public void replaceField(Field field){
		stage.replaceField(field);
	}
	public void newGame(File file) throws Exception{
		try {
			graphic.clear();
			status.clear();
			this.stage = new Stage(file, this);
			setLoop();
		} catch (Exception e){
			throw new Exception(e);
		}
	}
	public void newGame() throws Exception{
		try {
			graphic.clear();
			status.clear();
			this.stage = new Stage(new File("testMap.xml"), this);
			setLoop();
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
					status.update(getAllZPM(), Oneill.getCollectedZPM(), Jaffa.getCollectedZPM(), Oneill.getBox(), Jaffa.getBox());
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
	public void setStage(Stage stage){
		this.stage = stage;
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
		if(e.getKeyCode() == KeyEvent.VK_O){
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