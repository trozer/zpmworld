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
	// M�k�d�s
	@Override
	public void doo(Player player){

		ActionType actionType = player.getAction().getType();
		if(!checkAcceptance(player,actionType)){
			return;
		}

		switch (actionType) {
        	case MOVE:

				// Ha mindenki ellen�rizte a MOVE-t, akkor a Field elv�gzi annak alap�rtelmezett r�sz�t
				player.step(this);
				containedUnits.add(player);

				// Speci�lis cselekv�sek
				if (!containedUnits.isEmpty()){
					for(Unit unit : containedUnits){
						unit.accept(player,this);
					}
				}
        		break;
        
			case GRAB:	//ha a j�t�kos fel akar venni valamit
        		if (!containedUnits.isEmpty()){
					for(Unit unit : containedUnits){
						unit.accept(player,this);
					}
    			}
        		break;

			case DROP:
				// ha mindenki enged�lyezte a DROP-ot, akkor accept n�lk�l �tveszi a Road,
				// hiszen nincs speci�lis cselekv�s
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

		// Alap�rtelmezett cselekv�s: ha mindenki enged�lyezte, akkor semmi akad�lya
		bullet.step(this);
		containedUnits.add(bullet);

		// Speci�lis cselekv�s
		if(!containedUnits.isEmpty()) {
			for (Unit unit : containedUnits) {
				unit.accept(bullet, this);
			}
		}
	}

	@Override
	 public void doo(Replicator replicator){

		if(!checkAcceptance(replicator, ActionType.MOVE)){		//TODO nem felt�tlen kell egyszer�s�teni... szebb a MOVEos megold�s
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
		return "�t:" + super.toString();
	}
}
