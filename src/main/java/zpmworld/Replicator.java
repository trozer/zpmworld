package zpmworld;

import java.util.Random;

public class Replicator extends ActionUnit{
	
	private Game game;
	
	public Replicator(Game game, Direction direction, Field field){
		this.game = game;
		this.currentDirection = direction;
		this.currentField = field;
		this.weight = 15;
		this.nextAction = new Action(ActionType.MOVE, direction, null);
	}

	//A replicator uj action-jet allitja elo.
	private Action makeNextAction() {
		Direction dir;
		Random r = new Random();
		int i = r.nextInt(7); //0-6
		
		if (i < 3) dir = currentDirection;
		else if (i == 3) dir = Direction.NORTH;
		else if (i == 4) dir = Direction.EAST;
		else if (i == 5) dir = Direction.WEST;
		else dir = Direction.SOUTH;
		
		Action nextA = new Action(ActionType.MOVE, dir, null);
		return nextA;
	}
	
	//Kezdemenyezi a mezo cserejet (utra), majd megoli magat
	public void replaceField(){
		game.replaceField(currentField);
		currentField.removeUnit(this);
		kill();
	}
	
	//Eltavolitja magat a jelenlegi mezorol,
	//es beallitja a currentFieldet az uj mezore. Illetve beallit maganak egy uj mozgast.
	public void step(Field target){
		currentField.removeUnit();
		currentField = target;
		
		nextAction = makeNextAction();
	}
	
	//A nextAction alapjan csinal valamit.
	public void action() {
		//Ha move, akkor a megfelelo iranyban levo szomszedos mezo doo-jat hivja meg.
		if (nextAction.getType() == ActionType.MOVE){
			currentField.getNeighbourInDirection(currentDirection).doo(this);
			nextAction = null;
		}
	}
	
	@Override
	public void accept(Bullet unit, Field field){
		field.removeUnit(unit);
		field.removeUnit();
		unit.kill();
		this.kill();
	}
	
	public String toString(){
		String irany;
		String elet;
		
		if (currentDirection == Direction.NORTH) irany = "észak";
		else if (currentDirection == Direction.EAST) irany = "kelet";
		else if (currentDirection == Direction.SOUTH) irany = "dél";
		else irany = "nyugat";
		
		if (dead == false) elet = "él";
		else elet = "halott";
		
    	return "Replikátor, mozgás cselekvést akar végrehajtani, " + irany + " irányba néz, " + elet;
    }
}
