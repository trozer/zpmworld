package zpmworld;

import java.awt.Color;

public class Bullet extends ActionUnit{

    private Color color;

    public Bullet(Action action, Field currentField){
        this.nextAction = action;
        this.currentField = currentField;
        this.currentDirection = action.getDirection();
        this.color = action.getColor();
        this.weight = 0;
    }

    public Color getColor(){ return color; }

    @Override
    public void step(Field target){
    	currentField.removeUnit(this);
        target.forceAddUnit(this);
        this.setCurrentField(target);
    }

    public void action(){
        if (nextAction.getType() == ActionType.MOVE){
        	currentField.getNeighbourInDirection(currentDirection).doo(this);
        }
    }

    public void setCurrentField(Field field){
        this.currentField = field;
    }
    
    public String toString(){
    	String szin;
    	String irany;
    	String elet;
    	
    	if (color == Color.BLUE) szin = "blue";
    	else if (color == Color.YELLOW) szin = "yellow";
    	else if (color == Color.GREEN) szin = "green";
    	else szin = "red";
    	
    	if (currentDirection == Direction.NORTH) irany = "�szak";
		else if (currentDirection == Direction.EAST) irany = "kelet";
		else if (currentDirection == Direction.SOUTH) irany = "d�l";
		else irany = "nyugat";
    	
    	if (dead == false) elet = "�l";
		else elet = "halott";
    	
    	return "l�ved�k: mozg�s cselekv�st akar v�grehajtani, " + irany + " ir�nyba n�z, " + szin + ", " + elet;
    }

}