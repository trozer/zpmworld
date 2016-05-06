package zpmworld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Scale extends Field {

	//--------Attrib�tumok--------
	
	
	private Gate myGate;
	private int openLimit;
	private Set<Box> containedBoxes;
	
	//-------Met�dusok---------

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
	
	public void setGate(Gate gate){	//be�ll�tja a saj�t kapuj�t, azaz a hozz� tartoz� kaput, ha m�g nincs neki
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

	// M�k�d�s

	@Override
	public void doo(Player player){	//a j�t�kos cselekedet�re "reag�l"

		ActionType actionType = player.getAction().getType();
		if(!checkAcceptance(player,actionType)){
			return;
		}

		switch (actionType) {
			/**
			 * A Scale-en egyszerre lehet t�bb Box �s m�s Unit is
			 */
			case MOVE:

				// Ha mindenki ellen�rizte a MOVE-t, akkor a Field elv�gzi annak alap�rtelmezett r�sz�t
				player.step(this);
				containedUnits.add(player);

				// Speci�lis cselekv�sek
				if (!containedUnits.isEmpty()){
					Set<Unit> deleteUnits = new HashSet<Unit>();
					for(Unit unit : containedUnits){
						unit.accept(player,deleteUnits);
					}
					//takar�t�s
					for(Unit unit : deleteUnits){
						if(containedUnits.contains(unit)){
							containedUnits.remove(unit);
						}
					}
				}

				gateMechanism();
				break;

			case GRAB:	//ha a j�t�kos leszed r�la egy t�rgyat, a hozz� tartoz� kapu bez�rul

				if (!containedBoxes.isEmpty()){
					Set<Unit> deleteUnits = new HashSet<Unit>();
					for(Box box : containedBoxes){
						box.accept(player,deleteUnits);
					}
					//takar�t�s
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
					//takar�t�s
					for(Unit unit : deleteUnits){
						if(containedUnits.contains(unit)){
							containedUnits.remove(unit);
						}
					}
				}

				gateMechanism();
				break;

			/**
			 * A j�t�kos csak doboz tud lerakni, ami a Scale eset�n mindig siker�l, hiszen lehet stackelni a
			 * dobozokat �s �llhat m�sik Unit is a m�rlegen.
			 */
			case DROP:
				Box box = player.dropBox();
                if(box != null) {
                    box.setCurrentField(this);
                    containedBoxes.add(box);
                }

				gateMechanism();
				break;
    		
        default:	//minden m�s eset
        	break;
		}
	}

	/**
	 * A dobozok nem jelentenek akad�lyt, �gy csak a rajta l�v� m�sik elem(ek)-t�l f�gg, valamint, mivel epuszt�thatja
	 * a m�rlegen �ll� replik�tort, ellen�rizz�k az �sszs�lyt.
	 * @param bullet
     */
	@Override
	public void doo(Bullet bullet){		//l�ved�kre reag�l

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
			//takar�t�s
			for(Unit unit : deleteUnits){
				if(containedUnits.contains(unit)){
					containedUnits.remove(unit);
				}
			}
		}

		gateMechanism();
	}

	/**
	 * A replik�tor viselked�se ennek a f�ggv�nynek a megh�v�sakor csak MOVE lehet, �gy ezt nem ellen�rizz�k k�l�n,
	 * valamint a dobozok nem befoly�solj�k a viselked�s�t, csak a t�bbi egys�g, viszont mivel meg is �llhat, ez�rt
	 * sz�m�t a s�lya.
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
			//takar�t�s
			for(Unit unit : deleteUnits){
				if(containedUnits.contains(unit)){
					containedUnits.remove(unit);
				}
			}
		}

		gateMechanism();
	}

	private void gateMechanism(){
		int gross = 0;		//�sszs�ly

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
		int gross = 0;		//�sszs�ly

		for(Unit unit : containedUnits){
			gross += unit.getWeight();
		}

		for(Box box : containedBoxes){
			gross += box.getWeight();
		}

		if(myGate != null)
			if(gross >= openLimit)
				return "m�rleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
					+ openLimit + " s�lyhat�r, lenyomva , van hozz�kapcsolt kapu, " 
					+ containedUnits.size() + " darab t�rolt egys�g";
			else
				return "m�rleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
				+ openLimit + " s�lyhat�r, nincs lenyomva , van hozz�kapcsolt kapu, " 
				+ containedUnits.size() + " darab t�rolt egys�g";
		else
			if(gross >= openLimit)
					return "m�rleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
						+ openLimit + " s�lyhat�r, lenyomva , nincs hozz�kapcsolt kapu, " 
						+ containedUnits.size() + " darab t�rolt egys�g";
				else
					return "m�rleg: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
					+ openLimit + " s�lyhat�r, nincs lenyomva , nincs hozz�kapcsolt kapu, " 
					+ containedUnits.size() + " darab t�rolt egys�g";
	}*/
	@Override
	public String toString() {
		int gross = 0;		//�sszs�ly

		for(Unit unit : containedUnits){
			gross += unit.getWeight();
		}

		for(Box box : containedBoxes){
			gross += box.getWeight();
		}
		return "Scale(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " +
				containedUnits.size() + "db ; containedBoxes: " + containedBoxes.size() + " db ; �sszs�ly: " + gross +
				" ; openLimit: " + openLimit + " ; �llapot: " + (gross<openLimit ? "z�r" : "nyit");
	}
}
