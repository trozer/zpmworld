package zpmworld;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;

public abstract class Unit implements Comparable{

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

    public int compareTo(Object o) {
        return this.hashCode() - o.hashCode();
    }

    // Speciális cselekvés a target irányába, launcheren álló Unit felõl
	//TODO jobb elnevezés a target és launcher nem szerencsés
	//TODO field igazából nem is kell, hiszen az mindig a Unit.currentField...
	public void accept(Player player, Set<Unit> deleteUnits){}
	public void accept(Bullet bullet, Set<Unit> deleteUnits){}
	public void accept(Replicator replicator, Set<Unit> deleteUnits){}

	@Override
	public
	String toString(){
		return "UNIT VAGYOK";
	}

    public abstract Element getXmlElement(Document doc);
}
