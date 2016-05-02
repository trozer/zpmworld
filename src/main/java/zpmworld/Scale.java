package zpmworld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;

public class Scale extends Field {

	//--------Attrib�tumok--------
	
	
	private Gate myGate;
	int openLimit;
	boolean blockAddUnit;
	
	//-------Met�dusok---------
	
	
	
	public Scale(){	//konstruktor
		super();	
		this.myGate = null;
		this.openLimit = 15;
		this.blockAddUnit = false;
		//ez �gy csud�latos, sz�ljatok, ha vissza�rhatom :-)
	}
	
	public void setGate(Gate gate){	//be�ll�tja a saj�t kapuj�t, azaz a hozz� tartoz� kaput, ha m�g nincs neki
		if(myGate == null) 
			myGate = gate;
		else 
			System.out.println("setGate: m�r van be�ll�tva gate!");
	}
	
	@Override
	public void doo(Player player){	//a j�t�kos cselekedet�re "reag�l"
		int hatar = 0;
		switch (player.getAction().getType()) {
        case MOVE:	//ha a j�t�kos r�l�p mag�ra h�zza �s kinyitja a hozz� tartoz� kaput
		
        	if (!containedUnits.isEmpty()){
        		for(int i = 0; i < containedUnits.size(); i++) 
    				containedUnits.get(i).accept(this, player);
        	}
        	if (containedUnits.isEmpty()){
				player.step(this);
				containedUnits.add(player);
				if (myGate != null)
					myGate.open();
			}
        	break;
		
        case GRAB:	//ha a j�t�kos leszed r�la egy t�rgyat, a hozz� tartoz� kapu bez�rul
        	if (!containedUnits.isEmpty()){
        		for(int i = 0; i < containedUnits.size(); i++) 
    				containedUnits.get(i).accept(this, player);
    		}
        	for(int i = 0; i < containedUnits.size(); i++) 
				hatar += containedUnits.get(i).getWeight();
			if (hatar < openLimit && myGate != null){
    			myGate.close();
        	}
    		break;
    		
        default:	//minden m�s eset
        	//TODO
        	break;
		}
	}
	
	@Override
	public void doo(Bullet bullet){		//l�ved�kre reag�l
		
		switch (bullet.getAction().getType()) {
        case MOVE:	//mag�ra h�zza a l�ved�ket, a l�ved�k nem nyitja ki a kaput
        	if (containedUnits.isEmpty()){
        		bullet.step(this);
        		containedUnits.add(bullet);
        		break;
        	}
        	else{
        		for(int i = 0; i < containedUnits.size(); i++) 
    				containedUnits.get(i).accept(bullet, this);
        	}
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
	public boolean addUnit(Unit unit){
		int hatar = 0;
		if (!blockAddUnit){
		containedUnits.add(unit);
		}
		blockAddUnit = true;
			for(int i = 0; i < containedUnits.size(); i++) 
				containedUnits.get(i).accept(unit, this);
		blockAddUnit = false;
		
		for(Unit u : containedUnits) hatar += u.getWeight();
		if (hatar >= openLimit){
			if(myGate != null)
				myGate.open();
		}
		return true;
		/*if (containedUnits.size() < 3 ){
			containedUnits.add(unit);
			myGate.open();
			return true;
		}
		else
			return false;*/
	}

	@Override
	public void removeUnit(){	//elt�vol�tja a unitot ami a m�rlegen van, ilyenkor a kapu bez�rul 
		containedUnits = new ArrayList<Unit>();
		if(myGate != null)
			myGate.close();
	}
	
	@Override
	public void removeUnit(Unit unit){
		int hatar = 0;
		if (!containedUnits.isEmpty()){
			containedUnits.remove(unit);
			for(Unit u : containedUnits) hatar += u.getWeight();
		}
		if (hatar < openLimit){
			if(myGate != null)
				myGate.close();
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
	
	//TODO,testing phase, correct this
	@Override
	public String toString(){
		int sulyok = 0;
		for (int i = 0; i < containedUnits.size(); i++){
			sulyok += containedUnits.get(i).getWeight();
		}
		if(myGate != null)
			if(sulyok >= openLimit)
				return "m�rleg: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
					+ openLimit + " s�lyhat�r, lenyomva , van hozz�kapcsolt kapu, " 
					+ containedUnits.size() + " darab t�rolt egys�g";
			else
				return "m�rleg: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
				+ openLimit + " s�lyhat�r, nincs lenyomva , van hozz�kapcsolt kapu, " 
				+ containedUnits.size() + " darab t�rolt egys�g";
		else
			if(sulyok >= openLimit)
					return "m�rleg: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
						+ openLimit + " s�lyhat�r, lenyomva , nincs hozz�kapcsolt kapu, " 
						+ containedUnits.size() + " darab t�rolt egys�g";
				else
					return "m�rleg: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") poz�ci�, "
					+ openLimit + " s�lyhat�r, nincs lenyomva , nincs hozz�kapcsolt kapu, " 
					+ containedUnits.size() + " darab t�rolt egys�g";
	}
}
