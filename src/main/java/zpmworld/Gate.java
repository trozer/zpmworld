package zpmworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Gate extends Field {
	
	//--------Attrib�tumok--------
	
	private boolean opened;
	
	//-------Met�dusok---------
	
	public Gate(){	//konstruktor
		super();
	}
	
	public void open(){	//nyitottra �ll�tja (kinyitja) a kaput
		opened = true;
	}
	
	public void close(){	//bez�rja a kaput, a kapuban l�v� unitokat megsemmis�ti
		opened = false;
		for(int i = 0; i < containedUnits.size(); i++) 
			containedUnits.get(i).kill();
		containedUnits = new ArrayList<Unit>();
	}
	
	@Override
	public void doo(Player player){		//a j�t�kos cselekedet�re "reag�l"
		if (opened == true){
			switch (player.getAction().getType()) {
			case MOVE:	//ha a j�t�kos l�pni akar
				if (containedUnits.isEmpty()){
					player.step(this);
					containedUnits.add(player);
				}
				else{
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(this, player);
				}
				break;
    
			case GRAB:	//ha a j�t�kos fel akar venni valamit
				if (!containedUnits.isEmpty()){
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(this, player);
					}
				break;
    		
			default:	//minden m�s eset
    	//TODO
    	
				break;
			}
		}
	}
	
	@Override
	public void doo(Bullet bullet){		//a l�ved�k cselekedet�tre reag�l
		
		if (opened == true){
			switch (bullet.getAction().getType()) {
			case MOVE:	//ha meg�rkezik/l�pni akar
				if (containedUnits.isEmpty()){
					bullet.step(this);
					containedUnits.add(bullet);
				}
				else{
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(bullet, this);
				}
				break;
			default:	//minden m�s eset
				break;
			}
		
		}
		else{
			bullet.step(this);
			bullet.kill();
		}
	}
	
	@Override
	public void doo(Replicator replicator){
		if (opened == true){
			switch (replicator.getAction().getType()) {
			case MOVE:
				replicator.step(this);
				containedUnits.add(replicator);
				break;
			default:
				break;
			}
		}
	}

	Boolean isOpened(){
		return opened;
	}

	@Override
	public void forceAddUnit(Unit unit){
		containedUnits.add(unit);
	}
	
	@Override
	public boolean addUnit(Unit unit){	//elhelyez ez unitot a kapuban, ha az nyitva van �s nincs m�g benne/rajta semmi
		if (opened == true){
			if (containedUnits.isEmpty()){
				containedUnits.add(unit);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void removeUnit(){
		containedUnits = null;
	}
	
	@Override
	void removeUnit(Unit unit){
		if (!containedUnits.isEmpty()){
			containedUnits.remove(unit);
		}
	}
	
	@Override
	public Field getNeighbourInDirection(Direction dir) {
		// TODO Auto-generated method stub
		return super.getNeighbourInDirection(dir);
	}

	@Override
	public void addNeighbour(Direction direction, Field neighbour) {
		// TODO Auto-generated method stub
		super.addNeighbour(direction, neighbour);
	}
	
	@Override
	public String toString(){
		if (opened)
			return "kapu:" + " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
				+ "nyitva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
		else
			return "kapu:" + " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
				+ "z�rva, "
				+ containedUnits.size() + " darab t�rolt egys�g";
	}

}
