package zpmworld;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends ActionUnit{

	private Box box;
	private List<ZPM> zpm;
	private int allZPM;
	private Game game;
	private String name;

	//Konstruktor TODO több
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
	}

	public Player(int allZPM, Direction direction, Field currentField, Game game, String name, int weight){
		super(currentField, weight, direction, new Action(ActionType.NONE, null, null));
		this.allZPM = allZPM;
		this.game = game;
		this.name = name;
		this.zpm = new ArrayList<ZPM>();
		this.box = null;
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

	//ZPM felvetele. Ha elfogyott a palyarol az osszes, akkor nyertunk
	public void addZPM(ZPM zpm){
		this.zpm.add(zpm);
		if (name.equals("O'neill") && this.zpm.size() % 2 == 0){
			game.createZPM();
		}
		if (game.getAllZPM() == 0) {
			game.win();
		}
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
				nextAction = null; //TODO szebb lenne valami setAction(type color dir) és setAction(NONE, null, null)
				break;
			case TURN:
				currentDirection = nextAction.getDirection();
				nextAction = null;
				break;
			case SHOOT:
				if(target == null) return; //arra fog menni a lövedék
				Action action = new Action(ActionType.MOVE, currentDirection, nextAction.getColor());
				Bullet newBullet = new Bullet(action, currentField); // a lövedék létrejön a mezõn, amin a player áll, azért nem a következõ mezõn, mert akkor az ottani esetleges konfront nem lenne kezelve
				currentField.addUnit(newBullet); //ez igazából nem is szükséges..
				game.addUnit(newBullet);
				nextAction = null;
				break;
			default:
				break;
		}
	}

	/*@Override
	public void accept(Bullet bullet, Set<Unit> deleteUnits) {
		//TODO ha akarjuk h lövedékkel ki is lehessen nyírni a Playert, akkor itt lehet megcsinálni
	}*/

	//Meghal a jatekos, veget er a jatek.
	public void kill(){
		dead = true;
		game.lose();
	}

	public String toString(){
		String cselekves;
		String irany;
		String doboz;
		String elet;

		if (nextAction != null) {
			if (nextAction.getType() == ActionType.DROP) cselekves = "lerakás ";
			else if (nextAction.getType() == ActionType.GRAB) cselekves = "felvétel ";
			else if (nextAction.getType() == ActionType.MOVE) cselekves = "mozgás ";
			else if (nextAction.getType() == ActionType.SHOOT) cselekves = "lövés ";
			else if (nextAction.getType() == ActionType.TURN) cselekves = "fordulás ";
			else cselekves = "semmi ";
		}
		else cselekves = "semmi ";

		if (currentDirection == Direction.NORTH) irany = "észak";
		else if (currentDirection == Direction.EAST) irany = "kelet";
		else if (currentDirection == Direction.SOUTH) irany = "dél";
		else irany = "nyugat";

		if (box != null) doboz = "van nála doboz, ";
		else doboz = "nincs nála doboz, ";

		if (dead == false) elet = "él";
		else elet = "halott";

		String pos;
		if(currentField != null){
			 pos = "(" + currentField.getPosition().x + "," + currentField.getPosition().y + ")";
		} else {
			 pos = "(null,null)";
		}

		return name + ": " + pos + " ; " + cselekves + "cselekvést akar végrehajtani, " + irany + " irányba néz, " + weight + " tömegû, "
				+ doboz + zpm.size() + " db begyûjtött ZPM van nála, " + elet;
	}
}
