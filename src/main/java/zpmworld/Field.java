package zpmworld;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;
import java.awt.Point;

public abstract class Field {
	
	//--------Attribútumok--------
	
	protected Map<Direction, Field> neighbours;
	/**
	 *  Egyediség fontos: az accepteknél elő fog fordulni, h egy egységet többször is hozzá akarunk adni, de ezt így kivédjük.
	 */
	protected Set<Unit> containedUnits;
	protected Point position;
	
	//-------Metódusok---------

	// Konstruktorok:

	public Field(){
		neighbours = new TreeMap<Direction, Field>();
		neighbours.put(Direction.NORTH, null);
		neighbours.put(Direction.SOUTH, null);
		neighbours.put(Direction.WEST, null);
		neighbours.put(Direction.EAST, null);
		containedUnits = new HashSet<Unit>();
	}

	public Field(Point position){
		neighbours = new TreeMap<Direction, Field>();
		neighbours.put(Direction.NORTH, null);
		neighbours.put(Direction.SOUTH, null);
		neighbours.put(Direction.WEST, null);
		neighbours.put(Direction.EAST, null);
		containedUnits = new HashSet<Unit>();
		this.position = position;
	}

	// Getter-setter függvények:

	void setPosition(Point position){
		this.position = position;
	}
	
	Point getPosition(){
		return this.position;
	}

	public Set<Unit> getUnits(){
		return containedUnits;
	}

	//Visszaadja a pram�ter kapott ir�ny fel�li szomsz�dj�t
	public Field getNeighbourInDirection(Direction dir){
		for (Map.Entry<Direction, Field> entry : neighbours.entrySet()) {	//�gy l�pked�k v�gig egy Map objektumon
			if (entry.getKey() == dir){
				return entry.getValue();
			}
		}
		return null;	//ha nincs szomsz�dja a megadott ir�nyban
	}

	// Egyéb, attributum módosító metódusok:

	public void addNeighbour(Direction direction, Field neighbour){	//be�ll�tjuk egy mez� param�ter�l kapott ir�nybeli szomsz�dj�t, egy param�re�l kapott t�p�s� mez�re
		for (Map.Entry<Direction, Field> entry : neighbours.entrySet()) {
			if (entry.getKey() == direction){
				entry.setValue(neighbour);
			}
		}
	}

	/**
	 * Az addUnitot az acceptek hívják, és csak akkor, ha az adott egység engedélyezi,
	 * hogy mellérakják unitot: a Unit dönt, nem a Field, így ennek mindenképp sikerülnie kell, kivéve, ha már
	 * benne van az elem: de ezt a TreeSet.add megoldja
	 * @param unit új elem
     */
	public void addUnit(Unit unit){
		containedUnits.add(unit);
	}

    public void addUnit(Box box){
        return;
    }

	/**
	 * Paraméter nélkül minden elemet kitöröl containedUnitból
	 */
	public void removeUnit(){
		containedUnits.clear();
	}

	/**
	 * Kitörli unit-ot a containedUnitból
	 * @param unit törlendő elem
     */
	void removeUnit(Unit unit){
		if (!containedUnits.isEmpty()){
			containedUnits.remove(unit);
		}
	}

	// Működést vezérlő metódusok:
	
	public void doo(Player player){}
	public void doo(Bullet bullet){}
	public void doo(Replicator replicator){}

	protected boolean checkAcceptance(Unit unit, ActionType actionType){
		if(!containedUnits.isEmpty()) {
			for (Unit u : containedUnits) {
				if (!u.check(unit, actionType)) {
					return false;
				}
			}
		}
		return true;
	}
	// Teszteléshez használt metódusok:

	public void showUnits(){
		for(Unit unit : containedUnits)
			System.out.println(unit.toString());

	}
	
	/*@Override
	public String toString(){
		return " (" + (int)position.getX() + "," + (int)position.getY() + ") poz�ci�, "
				+ containedUnits.size() + " darab t�rolt egys�g";
		
	}*/

    public Element getXmlElement(Document doc){
        return null;
    }

	@Override
	public String toString() {
		return "Field(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " + containedUnits.size() + "db ";
	}

	public void removeLastAdded() {
		if(!containedUnits.isEmpty()){
			containedUnits.remove(((TreeSet)containedUnits).last());
		}
	}
}
