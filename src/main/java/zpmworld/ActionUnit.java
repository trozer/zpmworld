package zpmworld;

import java.awt.Color;

public abstract class ActionUnit extends Unit{
	protected Direction currentDirection;
	protected Action nextAction;

	protected ActionUnit(){
		super();
	}

	protected ActionUnit(int weight){
		super(weight);
	}

	protected ActionUnit(Field currentField, int weight){
		super(currentField, weight);
	}

	protected ActionUnit(Field currentField, int weight, Direction direction, Action nextAction){
		super(currentField,weight);
		this.currentDirection = direction;
		this.nextAction = nextAction;
	}

	// Getter-setter
	public Action getAction(){
		return nextAction;
	}

	public Direction getDirection(){
		return currentDirection;
	}

	//Mozgas tipusu action-t hoz letre.
	public void move(){
		nextAction = new Action(ActionType.MOVE, currentDirection, null);
	}
	
	//Forgas tipusu action-t hoz letre.
	public void turn(Direction direction){
		nextAction = new Action(ActionType.TURN, direction, null);
	}
	
	
	public void shoot(Color color){
		nextAction = new Action(ActionType.SHOOT, currentDirection, color);
	}
	
	public void grab(){
		nextAction = new Action(ActionType.GRAB, currentDirection, null);
	}

	public Direction getCurrentDirection(){ return currentDirection; }
	
	public void drop(){
		nextAction = new Action(ActionType.DROP, currentDirection, null);
	}
	
	public void step(Field target){
		if(currentField != null) {
			currentField.removeUnit(this);
		}
		currentField = target;
	}
}
