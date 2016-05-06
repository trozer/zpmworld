package zpmworld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Scale extends Field {

	//--------Attribútumok--------
	
	
	private Gate myGate;
	private int openLimit;
	private Set<Box> containedBoxes;
	
	//-------Metódusok---------

	public Scale(){	//konstruktor
		super();	
		this.myGate = null;
		this.openLimit = 15;
		containedBoxes = new TreeSet<Box>();
	}

	public Scale(int openLimit){	//konstruktor
		super();
		this.myGate = null;
		this.openLimit = openLimit;
		containedBoxes = new TreeSet<Box>();
	}

	public Scale(Point position){
		super(position);
		this.myGate = null;
		this.openLimit = 15;
		containedBoxes = new TreeSet<Box>();
	}

	public Scale(Point position, int openLimit){
		super(position);
		this.myGate = null;
		this.openLimit = openLimit;
		containedBoxes = new TreeSet<Box>();
	}

	// Getter-setter
	
	public void setGate(Gate gate){	//beállítja a saját kapuját, azaz a hozzá tartozó kaput, ha még nincs neki
		if(myGate == null) 
			myGate = gate;
	}

	public void addUnit(Box box){
		containedBoxes.add(box);
	}

	public void removeUnit(Box box){
		if (!containedBoxes.isEmpty()){
			containedBoxes.remove(box);
		}
	}

	public Set<Box> getBoxes(){
		return containedBoxes;
	}

	public int getOpenLimit(){
		return openLimit;
	}

	// Mûködés

	@Override
	public void doo(Player player){	//a játékos cselekedetére "reagál"

		ActionType actionType = player.getAction().getType();
		if(!checkAcceptance(player,actionType)){
			return;
		}

		switch (actionType) {
			/**
			 * A Scale-en egyszerre lehet több Box és más Unit is
			 */
			case MOVE:

				// Ha mindenki ellenõrizte a MOVE-t, akkor a Field elvégzi annak alapértelmezett részét
				player.step(this);
				containedUnits.add(player);

				// Speciális cselekvések
				if (!containedUnits.isEmpty()){
					Set<Unit> deleteUnits = new HashSet<Unit>();
					for(Unit unit : containedUnits){
						unit.accept(player,deleteUnits);
					}
					//takarítás
					for(Unit unit : deleteUnits){
						if(containedUnits.contains(unit)){
							containedUnits.remove(unit);
						}
					}
				}

				gateMechanism();
				break;

			case GRAB:	//ha a játékos leszed róla egy tárgyat, a hozzá tartozó kapu bezárul

				if (!containedBoxes.isEmpty()){
					Set<Unit> deleteUnits = new HashSet<Unit>();
					for(Box box : containedBoxes){
						box.accept(player,deleteUnits);
					}
					//takarítás
					for(Unit unit : deleteUnits){
						if(containedBoxes.contains(unit)){
							containedBoxes.remove(unit);
						}
					}
				}

				if (!containedUnits.isEmpty()){
					Set<Unit> deleteUnits = new HashSet<Unit>();
					for(Unit unit : containedUnits){
						unit.accept(player,deleteUnits);
					}
					//takarítás
					for(Unit unit : deleteUnits){
						if(containedUnits.contains(unit)){
							containedUnits.remove(unit);
						}
					}
				}

				gateMechanism();
				break;

			/**
			 * A játékos csak doboz tud lerakni, ami a Scale esetén mindig sikerül, hiszen lehet stackelni a
			 * dobozokat és állhat másik Unit is a mérlegen.
			 */
			case DROP:
				Box box = player.dropBox();
                if(box != null) {
                    box.setCurrentField(this);
                    containedBoxes.add(box);
                }

				gateMechanism();
				break;
    		
        default:	//minden más eset
        	break;
		}
	}

	/**
	 * A dobozok nem jelentenek akadályt, így csak a rajta lévõ másik elem(ek)-tõl függ, valamint, mivel epusztíthatja
	 * a mérlegen álló replikátort, ellenõrizzük az összsúlyt.
	 * @param bullet
     */
	@Override
	public void doo(Bullet bullet){		//lövedékre reagál

		if(!checkAcceptance(bullet, ActionType.MOVE)){
			return;
		}

		bullet.step(this);
		containedUnits.add(bullet);

		if (!containedUnits.isEmpty()){
			Set<Unit> deleteUnits = new HashSet<Unit>();
			for(Unit unit : containedUnits){
				unit.accept(bullet,deleteUnits);
			}
			//takarítás
			for(Unit unit : deleteUnits){
				if(containedUnits.contains(unit)){
					containedUnits.remove(unit);
				}
			}
		}

		gateMechanism();
	}

	/**
	 * A replikátor viselkedése ennek a függvénynek a meghívásakor csak MOVE lehet, így ezt nem ellenõrizzük külön,
	 * valamint a dobozok nem befolyásolják a viselkedését, csak a többi egység, viszont mivel meg is állhat, ezért
	 * számít a súlya.
	 * @param replicator
     */
	@Override
	 public void doo(Replicator replicator){

		if(!checkAcceptance(replicator, ActionType.MOVE)){
			return;
		}

		replicator.step(this);
		containedUnits.add(replicator);

		if (!containedUnits.isEmpty()){
			Set<Unit> deleteUnits = new HashSet<Unit>();
			for(Unit unit : containedUnits){
				unit.accept(replicator,deleteUnits);
			}
			//takarítás
			for(Unit unit : deleteUnits){
				if(containedUnits.contains(unit)){
					containedUnits.remove(unit);
				}
			}
		}

		gateMechanism();
	}

	private void gateMechanism(){
		int gross = 0;		//összsúly

		for(Unit unit : containedUnits){
			gross += unit.getWeight();
		}

		for(Box box : containedBoxes){
			gross += box.getWeight();
		}

		if(gross >= openLimit && myGate != null){
			myGate.open();
		} else if (myGate != null) {
			myGate.close();
		}
	}

	public int getBoxNum(){
		return containedBoxes.size();
	}

	@Override
	void removeUnit(Unit unit) {
		super.removeUnit(unit);
		gateMechanism();
	}

	//Teszt

	/*@Override
	public String toString(){
		int gross = 0;		//összsúly

		for(Unit unit : containedUnits){
			gross += unit.getWeight();
		}

		for(Box box : containedBoxes){
			gross += box.getWeight();
		}

		if(myGate != null)
			if(gross >= openLimit)
				return "mérleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") pozíció, "
					+ openLimit + " súlyhatár, lenyomva , van hozzákapcsolt kapu, " 
					+ containedUnits.size() + " darab tárolt egység";
			else
				return "mérleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") pozíció, "
				+ openLimit + " súlyhatár, nincs lenyomva , van hozzákapcsolt kapu, " 
				+ containedUnits.size() + " darab tárolt egység";
		else
			if(gross >= openLimit)
					return "mérleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") pozíció, "
						+ openLimit + " súlyhatár, lenyomva , nincs hozzákapcsolt kapu, " 
						+ containedUnits.size() + " darab tárolt egység";
				else
					return "mérleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") pozíció, "
					+ openLimit + " súlyhatár, nincs lenyomva , nincs hozzákapcsolt kapu, " 
					+ containedUnits.size() + " darab tárolt egység";
	}*/
	@Override
	public String toString() {
		int gross = 0;		//összsúly

		for(Unit unit : containedUnits){
			gross += unit.getWeight();
		}

		for(Box box : containedBoxes){
			gross += box.getWeight();
		}
		return "Scale(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " +
				containedUnits.size() + "db ; containedBoxes: " + containedBoxes.size() + " db ; összsúly: " + gross +
				" ; openLimit: " + openLimit + " ; állapot: " + (gross<openLimit ? "zár" : "nyit");
	}
}
