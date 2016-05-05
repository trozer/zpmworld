package zpmworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Gate extends Field {
	
	//--------Attribútumok--------
	
	private boolean opened;
	
	//-------Metódusok---------
	
	public Gate(){	//konstruktor
		super();
	}
	
	public void open(){	//nyitottra állítja (kinyitja) a kaput
		opened = true;
	}
	
	public void close(){	//bezárja a kaput, a kapuban lévõ unitokat megsemmisíti
		opened = false;
		for(int i = 0; i < containedUnits.size(); i++) 
			containedUnits.get(i).kill();
		containedUnits = new ArrayList<Unit>();
	}
	
	@Override
	public void doo(Player player){		//a játékos cselekedetére "reagál"
		if (opened == true){
			switch (player.getAction().getType()) {
			case MOVE:	//ha a játékos lépni akar
				if (containedUnits.isEmpty()){
					player.step(this);
					containedUnits.add(player);
				}
				else{
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(this, player);
				}
				break;
    
			case GRAB:	//ha a játékos fel akar venni valamit
				if (!containedUnits.isEmpty()){
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(this, player);
					}
				break;
    		
			default:	//minden más eset
    	//TODO
    	
				break;
			}
		}
	}
	
	@Override
	public void doo(Bullet bullet){		//a lövedék cselekedetétre reagál
		
		if (opened == true){
			switch (bullet.getAction().getType()) {
			case MOVE:	//ha megérkezik/lépni akar
				if (containedUnits.isEmpty()){
					bullet.step(this);
					containedUnits.add(bullet);
				}
				else{
					for(int i = 0; i < containedUnits.size(); i++) 
						containedUnits.get(i).accept(bullet, this);
				}
				break;
			default:	//minden más eset
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
	public boolean addUnit(Unit unit){	//elhelyez ez unitot a kapuban, ha az nyitva van és nincs még benne/rajta semmi
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
			return "kapu:" + " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") pozíció, "
				+ "nyitva, "
				+ containedUnits.size() + " darab tárolt egység";
		else
			return "kapu:" + " (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") pozíció, "
				+ "zárva, "
				+ containedUnits.size() + " darab tárolt egység";
	}

}
