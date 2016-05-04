package zpmworld;

import java.awt.Point;

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
					for(Unit unit : containedUnits){
						unit.accept(player,this);
					}
				}
        		break;
        
			case GRAB:	//ha a játékos fel akar venni valamit
        		if (!containedUnits.isEmpty()){
					for(Unit unit : containedUnits){
						unit.accept(player,this);
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
			for (Unit unit : containedUnits) {
				unit.accept(bullet, this);
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

		if(!containedUnits.isEmpty()){
			for(Unit unit : containedUnits){
				unit.accept(replicator, this);
			}
		}
	}
	
	@Override
	public String toString(){
		return "út:" + super.toString();
	}
}
