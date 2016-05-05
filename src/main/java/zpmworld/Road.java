package zpmworld;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Road extends Field {

	// Konstruktorok
	public Road() {
		super();
	}

	public Road(Point position){
		super(position);
	}
	// Mûködés
	@Override
	public void doo(Player player){

		ActionType actionType = player.getAction().getType();
		if(!checkAcceptance(player,actionType)){
			return;
		}

		switch (actionType) {
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
				box.setCurrentField(this);
				containedUnits.add(box);
				break;

        default:
        	break;
		}
	}

	@Override
	public void doo(Bullet bullet) {

		if(!checkAcceptance(bullet, ActionType.MOVE)){
			return;
		}

		// Alapértelmezett cselekvés: ha mindenki engedélyezte, akkor semmi akadálya
		bullet.step(this);
		containedUnits.add(bullet);

		// Speciális cselekvés
		if(!containedUnits.isEmpty()) {
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
	}

	@Override
	 public void doo(Replicator replicator){

		if(!checkAcceptance(replicator, ActionType.MOVE)){		//TODO nem feltétlen kell egyszerüsíteni... szebb a MOVEos megoldás
			return;
		}

		replicator.step(this);
		containedUnits.add(replicator);

		// Speciális cselekvés
		if(!containedUnits.isEmpty()) {
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
	/*
	@Override
	public String toString(){
		return "út:" + super.toString();
	}*/

	@Override
	public String toString() {
		return "Road(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " + containedUnits.size() + "db ";
	}
}
