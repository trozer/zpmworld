package zpmworld;

import java.awt.Point;

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
	}

	@Override
	public void doo(Bullet bullet){		//a l�ved�k cselekedet�tre reag�l

		if (opened == true){
			if(!checkAcceptance(bullet, ActionType.MOVE)){
				return;
			}

			bullet.step(this);
			containedUnits.add(bullet);

			// Speciális cselekvés
			if(!containedUnits.isEmpty()) {
				for (Unit unit : containedUnits) {
					unit.accept(bullet, this);
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

			replicator.step(this);
			containedUnits.add(replicator);

			if(!containedUnits.isEmpty()){
				for(Unit unit : containedUnits){
					unit.accept(replicator, this);
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

	@Override
	public String toString(){
		if (opened)
			return "kapu:" + " (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
				+ "nyitva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
		else
			return "kapu:" + " (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, "
				+ "z�rva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
	}

}
