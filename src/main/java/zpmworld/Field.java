package zpmworld;

import java.util.*;
import java.awt.Point;

public abstract class Field {
	
	//--------Attribútumok--------
	
	protected Map<Direction, Field> neighbours = new TreeMap<Direction, Field>();
	protected List<Unit> containedUnits;
	//protected Unit containedUnit; // DELETE THIS LATER, this helped me to avoid to change many functions in other classes
	//egyenlõre a fenti sort nem töröltem, mert nem akarom, hogy azok az osztályok miatt amiket nem én írtam hiba legyen benne
	protected Point position;
	
	//-------Metódusok---------
	
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
	
	//Visszaadja a praméter kapott irány felõli szomszédját
	public Field getNeighbourInDirection(Direction dir){
		for (Map.Entry<Direction, Field> entry : neighbours.entrySet()) {	//így lépkedük végig egy Map objektumon
			if (entry.getKey() == dir){
				
				return entry.getValue();
			}
		}
		return null;	//ha nincs szomszédja a megadott irányban
	}
	
	public boolean addUnit(Unit unit){	//új unitot adunk a mezõhöz; csak akkor tehetjük meg, ha az üres
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
	
	public void addNeighbour(Direction direction, Field neighbour){	//beállítjuk egy mezõ paraméterül kapott iránybeli szomszédját, egy paraméreül kapott típúsú mezõre
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
		return " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") pozíció, "
				+ containedUnits.size() + " darab tárolt egység";
		
	}
	
}
