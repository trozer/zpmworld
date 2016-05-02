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
    	
    	if (currentDirection == Direction.NORTH) irany = "észak";
		else if (currentDirection == Direction.EAST) irany = "kelet";
		else if (currentDirection == Direction.SOUTH) irany = "dél";
		else irany = "nyugat";
    	
    	if (dead == false) elet = "él";
		else elet = "halott";
    	
    	return "lövedék: mozgás cselekvést akar végrehajtani, " + irany + " irányba néz, " + szin + ", " + elet;
    }

}