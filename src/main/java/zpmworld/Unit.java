package zpmworld;

import java.util.*;

public abstract class Unit{

	// Attributumok

	protected boolean dead;
	protected Field currentField;
	protected int weight;
	protected Map<Class,Map<ActionType,Boolean>> acceptTable;

	/**
	 * Van konstruktor, de csak a lesz�rmazottak haszn�lj�k attributom kezd��rt�kek be�ll�t�s�ra.
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

	// Getter - setter f�ggv�nyek

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

	// M�k�d�st vez�rl� f�ggv�nyek

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

	// Speci�lis cselekv�s a target ir�ny�ba, launcheren �ll� Unit fel�l
	//TODO jobb elnevez�s a target �s launcher nem szerencs�s
	//TODO field igaz�b�l nem is kell, hiszen az mindig a Unit.currentField...
	public void accept(Player player, Set<Unit> deleteUnits){}
	public void accept(Bullet bullet, Set<Unit> deleteUnits){}
	public void accept(Replicator replicator, Set<Unit> deleteUnits){}

	@Override
	public
	String toString(){
		return "UNIT VAGYOK";
	}
}
