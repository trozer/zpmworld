package zpmworld;

import java.awt.Color;
import java.io.IOException;
import java.util.*;

public class Player extends ActionUnit{

	private Box box;
	private List<ZPM> zpm;
	private int allZPM;
	private Game game;
	private String name;
    private boolean enabledToKill;      //ha true, a Player l�v�sei hal�losak a m�sik Player sz�m�ra

    //Konstruktor TODO t�bb
	public Player(int allZPM, Direction direction, Action action, Field currentField, Game game, Box box, String name){
		super(currentField, 15);
		this.box = box;
		this.allZPM = allZPM;
		this.currentDirection = direction;
		this.currentField = currentField;
		this.game = game;
		this.zpm = new ArrayList<ZPM>();
		this.name = name;
		this.nextAction = action;
        this.enabledToKill = false;
	}

	public Player(int allZPM, Direction direction, Field currentField, Game game, String name, int weight){
		super(currentField, weight, direction, new Action(ActionType.NONE, null, null));
		this.allZPM = allZPM;
		this.game = game;
		this.name = name;
		this.zpm = new ArrayList<ZPM>();
		this.box = null;
        this.enabledToKill = false;
	}

	@Override
	protected Map<Class, Map<ActionType, Boolean>> initAcceptance() {
		Map<Class,Map<ActionType,Boolean>> returnMap = new HashMap<Class, Map<ActionType, Boolean>>();
		Map<ActionType, Boolean> playerAccept = new HashMap<ActionType, Boolean>();
		playerAccept.put(ActionType.NONE,true);
		returnMap.put(Player.class,playerAccept);
		Map<ActionType, Boolean> bulletAccept = new HashMap<ActionType, Boolean>();
		bulletAccept.put(ActionType.MOVE,true);
		bulletAccept.put(ActionType.NONE,true);
		returnMap.put(Bullet.class,bulletAccept);
		Map<ActionType, Boolean> replicatorAccept = new HashMap<ActionType, Boolean>();
		replicatorAccept.put(ActionType.MOVE,true);
		replicatorAccept.put(ActionType.NONE,true);
		returnMap.put(Replicator.class,replicatorAccept);

		return returnMap;
	}

	//Oszzegyujtott ZPM-ek szamat adja vissza
	public int getCollectedZPM(){
		return zpm.size();
	}

	public Box getBox() {
		return box;
	}

	public String getName() {
		return name;
	}

    public boolean getEnabledToKill() {
        return enabledToKill;
    }

	//ZPM felvetele. Ha elfogyott a palyarol az osszes, akkor nyertunk
	public void addZPM(ZPM zpm){
		this.zpm.add(zpm);
        enabledToKill = game.enableToKill(this);
		if (name.equals("O'neill") && this.zpm.size() % 2 == 0){
			game.createZPM();
		}
		if (game.getAllZPM() == 0) {
            if(game.ONeillWon()) {
                game.win();
            } else {
                game.lose();
            }
		}
	}

    public void addZPMSimply(ZPM zpm){
        this.zpm.add(zpm);
    }

	//Doboz lerakasa.
	public Box dropBox(){
		Box temp = this.box;
		this.box = null;
		return temp;
	}

	//Doboz felvetele csak akkor, ha meg nincs nala doboz.
	public boolean grabBox(Box box){
		if (this.box == null){
			this.box = box;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void action() {
		if(nextAction == null || nextAction.getType() == ActionType.NONE) {
			return;
		}

		Field target = currentField.getNeighbourInDirection(currentDirection);
		switch (nextAction.getType()) {
			case MOVE:
			case GRAB:
			case DROP:
				if(target == null) return;
				target.doo(this);
				nextAction = null; //TODO szebb lenne valami setAction(type color dir) �s setAction(NONE, null, null)
				break;
			case TURN:
				currentDirection = nextAction.getDirection();
				nextAction = null;
				break;
			case SHOOT:
				if(target == null) return; //arra fog menni a l�ved�k
				Action action = new Action(ActionType.MOVE, currentDirection, nextAction.getColor());
				Bullet newBullet = new Bullet(action, currentField); // a l�ved�k l�trej�n a mez�n, amin a player �ll, az�rt nem a k�vetkez� mez�n, mert akkor az ottani esetleges konfront nem lenne kezelve
				currentField.addUnit(newBullet); //ez igaz�b�l nem is sz�ks�ges..
				game.createBullet(newBullet);
				try {
					game.registerDrawableUnit(new DrawableBullet(newBullet));
				} catch (IOException e) {
					e.printStackTrace();
				}
				nextAction = null;
				break;
			default:
				break;
		}
	}

	@Override
	public void accept(Bullet bullet, Set<Unit> deleteUnits) {
        if(game.killEnabledToEnemy(this)){
            deleteUnits.add(bullet);
            deleteUnits.add(this);
            bullet.kill();
            this.kill();
        }
	}

	//Meghal a jatekos, veget er a jatek.
	public void kill(){
		dead = true;
        if(name == "O'neill"){
            game.lose();
        } else {
            game.win();
        }

	}

	public String toString(){
		String cselekves;
		String irany;
		String doboz;
		String elet;

		if (nextAction != null) {
			if (nextAction.getType() == ActionType.DROP) cselekves = "lerak�s ";
			else if (nextAction.getType() == ActionType.GRAB) cselekves = "felv�tel ";
			else if (nextAction.getType() == ActionType.MOVE) cselekves = "mozg�s ";
			else if (nextAction.getType() == ActionType.SHOOT) cselekves = "l�v�s ";
			else if (nextAction.getType() == ActionType.TURN) cselekves = "fordul�s ";
			else cselekves = "semmi ";
		}
		else cselekves = "semmi ";

		if (currentDirection == Direction.NORTH) irany = "�szak";
		else if (currentDirection == Direction.EAST) irany = "kelet";
		else if (currentDirection == Direction.SOUTH) irany = "d�l";
		else irany = "nyugat";

		if (box != null) doboz = "van n�la doboz, ";
		else doboz = "nincs n�la doboz, ";

		if (dead == false) elet = "�l";
		else elet = "halott";

		String pos;
		if(currentField != null){
			 pos = "(" + currentField.getPosition().x + "," + currentField.getPosition().y + ")";
		} else {
			 pos = "(null,null)";
		}

		return name + ": " + pos + " ; " + cselekves + "cselekv�st akar v�grehajtani, " + irany + " ir�nyba n�z, " + weight + " t�meg�, "
				+ doboz + zpm.size() + " db begy�jt�tt ZPM van n�la, " + elet;
	}

}
