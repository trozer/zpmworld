package zpmworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.text.Position;

public class Road extends Field {

	// --------Attrib�tumok--------


	


	// -------Met�dusok---------

	public Road() {	//konstruktor
		super();
	}

	@Override
	public void doo(Player player){	//a j�t�kos cselekedet�re "reag�l"
		player.getAction();
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
		//drop -adunit int�zi
	}

	@Override
	public void doo(Bullet bullet) {	//a l�ved�k cselekedet�tre reag�l
		switch (bullet.getAction().getType()) {
        case MOVE:	//ha meg�rkezik/l�pni akar
        	if (containedUnits.isEmpty()){
        	bullet.step(this);
        	
        	}
        	else{
        		for(int i = 0; i < containedUnits.size(); i++) 
    				containedUnits.get(i).accept(bullet, this);
        		bullet.step(this);
			}
        	break;
        default:	//minden m�s eset
        	break;
		}
	}

	@Override
	 public void doo(Replicator replicator){
		switch (replicator.getAction().getType()) {
        case MOVE:
        	replicator.step(this);
        	containedUnits.add(replicator);
        	break;
    	default:
			break;
		}
	}
	
	@Override
	public void forceAddUnit(Unit unit){
		containedUnits.add(unit);
	}

	@Override
	public void removeUnit(){
		containedUnits = new ArrayList<Unit>();
	}
	
	@Override
	void removeUnit(Unit unit){
		if (!containedUnits.isEmpty()){
			containedUnits.remove(unit);
		}
	}
	
	@Override
	public boolean addUnit(Unit unit){
		if(containedUnits.isEmpty()){
			containedUnits.add(unit);
			return true;
		}
		return false;
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
		return "�t:" + super.toString();
	}
}
