package zpmworld;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Replicator extends ActionUnit{
	
	private Game game;
	
	public Replicator(Game game, Direction direction, Field field){
		super();
		this.game = game;
		this.currentDirection = direction;
		this.currentField = field;
		this.weight = 15;
		this.nextAction = new Action(ActionType.MOVE, direction, null);
	}

	public Replicator(Game game, Direction direction, Field currentField, int weight){
		super(currentField, weight, direction, new Action(ActionType.MOVE, direction, null));
		this.game = game;
	}

	@Override
	protected Map<Class, Map<ActionType, Boolean>> initAcceptance() {
		Map<Class,Map<ActionType,Boolean>> returnMap = new HashMap<Class, Map<ActionType, Boolean>>();
		Map<ActionType, Boolean> playerAccept = new HashMap<ActionType, Boolean>();
		playerAccept.put(ActionType.MOVE,true);
		playerAccept.put(ActionType.GRAB,true);
		playerAccept.put(ActionType.DROP,true);    //olvashatóság miatt van külön kiemelve, de ha nem lenne itt, akkor is false lenne
		playerAccept.put(ActionType.NONE,true);
		returnMap.put(Player.class,playerAccept);
		Map<ActionType, Boolean> bulletAccept = new HashMap<ActionType, Boolean>();
		bulletAccept.put(ActionType.MOVE,true);
		bulletAccept.put(ActionType.NONE,true);
		returnMap.put(Bullet.class,bulletAccept);
		Map<ActionType, Boolean> replicatorAccept = new HashMap<ActionType, Boolean>();
		replicatorAccept.put(ActionType.MOVE,true);
		replicatorAccept.put(ActionType.NONE,true);
		returnMap.put(Replicator.class,replicatorAccept);

		return returnMap;
	}

	//A nextAction alapjan csinal valamit.
	public void action() {
		if(nextAction == null || nextAction.getType() == ActionType.NONE) {
			return;
		}
		//Ha move, akkor a megfelelo iranyban levo szomszedos mezo doo-jat hivja meg.
		if (nextAction.getType() == ActionType.MOVE){
			currentField.getNeighbourInDirection(currentDirection).doo(this);
			nextAction = makeNextAction();
		} else if (nextAction.getType() == ActionType.TURN){
			nextAction = makeNextAction();
		} else {
			nextAction = makeNextAction();
		}
	}

	// Speciális cselekvés bullet esetén
	@Override
	public void accept(Bullet bullet, Set<Unit> deleteUnits){
		deleteUnits.add(bullet);
		deleteUnits.add(this);
		bullet.kill();
		this.kill();
	}

	//Kezdemenyezi a mezo cserejet (utra), majd megoli magat
	public void replaceField(){
		game.replaceField(currentField);
		currentField.removeUnit(this);
		kill();
	}

	//A replicator uj action-jet allitja elo.
	//TODO okosítani: zsákutca = megfordul, elágazás: nagyobb eséllyel nem fordul vissza...pl previousPosition attrib segíthet
	private Action makeNextAction() {
		int randomAction = new Random().nextInt(10); 	//0-9
		if(randomAction < 7){	//70% eséllyel MOVE
			return new Action(ActionType.MOVE,currentDirection,null);
		}

		Direction dir;
		Random r = new Random();
		int i = r.nextInt(4); //0-3

		if (i == 0) dir = Direction.NORTH;
		else if (i == 1) dir = Direction.EAST;
		else if (i == 2) dir = Direction.WEST;
		else dir = Direction.SOUTH;

		return new Action(ActionType.TURN, dir, null);
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
