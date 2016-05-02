package zpmworld;

import java.util.*;
import java.awt.Point;

public abstract class Field {
	
	//--------Attrib�tumok--------
	
	protected Map<Direction, Field> neighbours = new TreeMap<Direction, Field>();
	protected List<Unit> containedUnits;
	//protected Unit containedUnit; // DELETE THIS LATER, this helped me to avoid to change many functions in other classes
	//egyenl�re a fenti sort nem t�r�ltem, mert nem akarom, hogy azok az oszt�lyok miatt amiket nem �n �rtam hiba legyen benne
	protected Point position;
	
	//-------Met�dusok---------
	
	Field(){
		neighbours.put(Direction.NORTH, null);
		neighbours.put(Direction.SOUTH, null);
		neighbours.put(Direction.WEST, null);
		neighbours.put(Direction.EAST, null);
		containedUnits = new ArrayList<Unit>();
	}
	
	void setPosition(Point position){
		this.position = position;
	}
	
	Point getPosition(){
		return this.position;
	}
	
	public void doo(Player player){}
	public void doo(Bullet bullet){}
	public void doo(Replicator replicator){}
	
	public List<Unit> getUnits(){
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
	
	public boolean addUnit(Unit unit){	//�j unitot adunk a mez�h�z; csak akkor tehetj�k meg, ha az �res
		//TODO -> this is (3) not definitive 
		if (containedUnits.isEmpty()){
			containedUnits.add(unit);
			return true;
		}
		else
			return false;
	}
	
	//TODO, this is for testing, delete or change this later
	public void showUnits(){
		for(int i = 0; i < containedUnits.size(); i++) 
			System.out.println(containedUnits.get(i).toString());
		
	}
	
	public void removeUnit(){	
		containedUnits = new ArrayList<Unit>();
	}
	
	void removeUnit(Unit unit){
		if (!containedUnits.isEmpty()){
			containedUnits.remove(unit);
		}
	}
	
	public void addNeighbour(Direction direction, Field neighbour){	//be�ll�tjuk egy mez� param�ter�l kapott ir�nybeli szomsz�dj�t, egy param�re�l kapott t�p�s� mez�re
		for (Map.Entry<Direction, Field> entry : neighbours.entrySet()) {
			if (entry.getKey() == direction){
				entry.setValue(neighbour);
			}
		}
	}
	
	public void forceAddUnit(Unit unit){
		containedUnits.add(unit);
	}
	
	public int toInt(double d){
		int x = (int) d;
		return x;
	}
	
	@Override
	public String toString(){
		return " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
				+ containedUnits.size() + " darab t�rolt egys�g";
		
	}
	
}
