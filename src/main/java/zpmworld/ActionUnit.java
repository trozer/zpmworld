package zpmworld;

import java.awt.Color;

public abstract class ActionUnit extends Unit{
	protected Direction currentDirection;
	protected Action nextAction;
	
	//Mozgas tipusu action-t hoz letre.
	public void move(){
		nextAction = new Action(ActionType.MOVE, currentDirection, null);
	}
	
	//Forgas tipusu action-t hoz letre.
	public void turn(Direction direction){
		nextAction = new Action(ActionType.TURN, direction, null);
	}
	
	
	public void shoot(Color color){}
	
	public void grab(){}
	
	public void drop(){}
	
	public void step(Field target){}
	
	public Action getAction(){
		return nextAction;
	}
	
	
}
