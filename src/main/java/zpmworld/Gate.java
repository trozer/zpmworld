package zpmworld;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Gate extends Field {
	
	//--------Attrib�tumok--------
	
	private boolean opened;

	//-------Met�dusok---------

	public Gate(){	//konstruktor
		super();
		opened = false;
	}

	public Gate(Point position){	//konstruktor
		super(position);
		opened = false;
	}

	public boolean isOpened(){
		return opened;
	}

	@Override
	public void doo(Player player){		//a j�t�kos cselekedet�re "reag�l"
		if (opened == true){

			ActionType actionType = player.getAction().getType();
			if(!checkAcceptance(player,actionType)){
				return;
			}

			switch (actionType) {
				case MOVE:
                    containedUnits.add(player);
					player.step(this);


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
					break;

				case GRAB:	//ha a játékos fel akar venni valamit
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
					break;

				case DROP:
					// ha mindenki engedélyezte a DROP-ot, akkor accept nélkül átveszi a Road,
					// hiszen nincs speciális cselekvés
					Box box = player.dropBox();
                    if(box != null) {
                        box.setCurrentField(this);
                        containedUnits.add(box);
                    }
					break;

				default:
					break;
			}
		}
	}

	@Override
	public void doo(Bullet bullet){		//a l�ved�k cselekedet�tre reag�l

		if (opened == true){
			if(!checkAcceptance(bullet, ActionType.MOVE)){
				return;
			}

            containedUnits.add(bullet);
            bullet.step(this);

			// Speciális cselekvés
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

		} else {
			bullet.step(this);
			bullet.kill();
		}
	}

	@Override
	public void doo(Replicator replicator){
		if (opened == true){
			if(!checkAcceptance(replicator, ActionType.MOVE)){		//TODO nem feltétlen kell egyszerüsíteni... szebb a MOVEos megoldás
				return;
			}

            containedUnits.add(replicator);
			replicator.step(this);

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
		}
	}

	public void open(){	//nyitottra �ll�tja (kinyitja) a kaput
		opened = true;
	}

	public void close(){	//bez�rja a kaput, a kapuban l�v� unitokat megsemmis�ti
		opened = false;

		for(Unit unit : containedUnits){
			unit.kill();
		}

		containedUnits.clear();
	}


	/*@Override
	public String toString(){
		if (opened)
			return "kapu:" + " (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
				+ "nyitva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
		else
			return "kapu:" + " (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
				+ "z�rva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
	}*/

	@Override
	public String toString() {
		return "Gate(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() +
				") ; containedUnits: " + containedUnits.size() + "db ; opened=" + opened;
	}
}
