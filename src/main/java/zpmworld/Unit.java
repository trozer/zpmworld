package zpmworld;

import java.util.HashMap;
import java.util.Map;

public abstract class Unit {

	// Attributumok

	protected boolean dead;
	protected Field currentField;
	protected int weight;
	protected Map<Class,Map<ActionType,Boolean>> acceptTable;

	/**
	 * Van konstruktor, de csak a leszármazottak használják attributom kezdõértékek beállítására.
	 */

	protected Unit(){
		dead = false;
		acceptTable = initAcceptance();
	}

	protected Unit(int weight){
		this.dead = false;
		this.weight = weight;
		acceptTable = initAcceptance();
	}

	protected Unit(Field currentField, int weight){
		this.dead = false;
		this.weight = weight;
		this.currentField = currentField;
		acceptTable = initAcceptance();
	}

	protected abstract Map<Class,Map<ActionType,Boolean>> initAcceptance();
	/*protected Map<Class,Map<ActionType,Boolean>> initAcceptance(){
		Map<Class,Map<ActionType,Boolean>> returnMap = new HashMap<Class, Map<ActionType, Boolean>>();
		Map<ActionType, Boolean> playerAccept = new HashMap<ActionType, Boolean>();
		playerAccept.put(ActionType.MOVE,false);
		playerAccept.put(ActionType.GRAB,false);
		playerAccept.put(ActionType.DROP,false);
		playerAccept.put(ActionType.TURN,false);
		playerAccept.put(ActionType.SHOOT,false);
		playerAccept.put(ActionType.NONE,false);
		returnMap.put(Player.class,playerAccept);
		Map<ActionType, Boolean> bulletAccept = new HashMap<ActionType, Boolean>();
		bulletAccept.put(ActionType.MOVE,false);
		bulletAccept.put(ActionType.NONE,false);
		returnMap.put(Bullet.class,bulletAccept);
		Map<ActionType, Boolean> replicatorAccept = new HashMap<ActionType, Boolean>();
		replicatorAccept.put(ActionType.MOVE,false);
		replicatorAccept.put(ActionType.NONE,false);
		returnMap.put(Replicator.class,replicatorAccept);

		return returnMap;
	}*/

	// Getter - setter függvények

	public boolean isDead(){
		return dead;
	}

	public Field getCurrentField(){
		return currentField;
	}

	public void setCurrentField(Field field){
		currentField = field;
	}

	public int getWeight(){
		return weight;
	}

	// Mûködést vezérlõ függvények

	public void action(){}
	
	public void kill(){
		dead = true;
	}

	public boolean check(Unit unit, ActionType actionType){
		Map<ActionType,Boolean> tempMap = acceptTable.get(unit.getClass());
		if(tempMap == null){
			return false;
		}

		Boolean returnBool = tempMap.get(actionType);
		if(returnBool == null){
			return false;
		}
		return returnBool;
	}

	// Speciális cselekvés a target irányába, launcheren álló Unit felõl
	//TODO jobb elnevezés a target és launcher nem szerencsés
	//TODO field igazából nem is kell, hiszen az mindig a Unit.currentField...
	public void accept(Player target, Field launcher){}
	public void accept(Bullet target, Field launcher){}
	public void accept(Replicator target, Field launcher){}
	//TODO ?kell ez?
	public void accept(Field launcher, Player target){}

	@Override
	public
	String toString(){
		return "UNIT VAGYOK";
	}
}
