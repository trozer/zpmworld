package zpmworld;

import java.awt.Color;

public class Action {
	private Color color;
	private ActionType type;
	private Direction direction;
	
	public Action(ActionType type, Direction direction, Color color){
		this.color = color;
		this.type = type;
		this.direction = direction;
	}
	
	public ActionType getType(){
		return type;
	}
	
	public Color getColor(){
		return color;
	}
	
	public Direction getDirection(){
		return direction;
	}
}
