package zpmworld;

import java.awt.Color;
import java.util.ArrayList;

public class Player extends ActionUnit{
	
	private Box box;
	private ArrayList<ZPM> zpm;
	private int allZPM;
	private Game game;
	private String name;
	
	//Konstruktor
	public Player(int allZPM, Direction direction,Action action, Field currentField, Game game, Box box, String name){
		this.box = box;
		this.allZPM = allZPM;
		this.currentDirection = direction;
		this.currentField = currentField;
		this.game = game;
		this.zpm = new ArrayList<ZPM>();
		this.weight = 15;
		this.name = name;
	}
	
	//Oszzegyujtott ZPM-ek szamat adja vissza
	public int getCollectedZPM(){
		return zpm.size();
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
	
	//Doboz lerakasa.
	public void dropBox(){
		this.box = null;
	}
	
	//Loves tipusu action-t hoz letre a parameterkent kapott szinnel.
	public void shoot(Color color){
		nextAction = new Action(ActionType.SHOOT, currentDirection, color);
	}
	
	//Felvetel tipusu action-t hoz letre.
	public void grab(){
		nextAction = new Action(ActionType.GRAB, currentDirection, null);
	}
	
	//Lerakas tipus action-t hoz letre.
	public void drop(){
		nextAction = new Action(ActionType.DROP, currentDirection, null);
	}
	
	//Meghal a jatekos, veget er a jatek.
	public void kill(){
		dead = true;
		game.lose();
	}
	
	//Eltavolitja magat a jelenlegi mezorol, majd hozzaadja magat a parameterkent kapott uj mezohoz,
	//es beallitja a currentFieldet az uj mezore.
	public void step(Field target){
		currentField.removeUnit();
		currentField = target;
	}
	//A nextAction alapjan csinal valamit.
	public void action(){
		//Ha move vagy grab, akkor a megfelelo iranyban levo szomszedos mezo doo-jat hivja meg.
		if(nextAction == null) return;
		if (nextAction.getType() == ActionType.MOVE ||
			nextAction.getType() == ActionType.GRAB){
				currentField.getNeighbourInDirection(currentDirection).doo(this);
				nextAction = null;
		}
		//Ha turn, akkor beallitja az uj iranyt.
		else if (nextAction.getType() == ActionType.TURN){
			currentDirection = nextAction.getDirection();
			nextAction = null;
		}
		//Ha drop, es van nala doboz, akkor megprobalja lerakni a megfelelo szomszedos mezore. Ha sikerult akkor a sajat box null lesz.
		else if (nextAction.getType() == ActionType.DROP && box != null){
			boolean success = currentField.getNeighbourInDirection(currentDirection).addUnit(box);
			if (success) box = null;
			nextAction = null;
		}
		//Ha shoot, letrehozzuk a lovedeket es atadjuk a game-nek.
		else if (nextAction.getType() == ActionType.SHOOT){
			Action act = new Action(ActionType.MOVE, currentDirection, nextAction.getColor());
			Bullet newBullet = new Bullet(act, currentField.getNeighbourInDirection(currentDirection));
			currentField.getNeighbourInDirection(currentDirection).forceAddUnit(newBullet);
			game.addUnit(newBullet);
			nextAction = null;
		}
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
		
    	return name + ": " + cselekves + "cselekvést akar végrehajtani, " + irany + " irányba néz, " + weight + " tömegû, "
    			+ doboz + zpm.size() + " db begyûjtött ZPM van nála, " + elet;
    }
}
