package zpmworld;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Bullet extends ActionUnit{

    private Color color;

    public Bullet(Action action, Field currentField){
        super();
        this.nextAction = action;
        this.currentField = currentField;
        this.currentDirection = action.getDirection();
        this.color = action.getColor();
        this.weight = 0;
    }

    public Bullet(Field currentField, int weight, Direction direction, Action nextAction, Color color){
        super(currentField, weight, direction, nextAction);
        this.color = color;
    }

    public Bullet(Field currentField, int weight, Direction direction, Color color){
        super(currentField, weight, direction, new Action(ActionType.MOVE, direction, color));
        this.color = color;
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

    public Color getColor(){ return color; }

    public void action(){
        if (nextAction.getType() == ActionType.MOVE){
        	currentField.getNeighbourInDirection(currentDirection).doo(this);
        } else { //ez csak biztosíték, elméletileg soha nem fut le
            this.nextAction = new Action(ActionType.MOVE, currentDirection, color);
            currentField.getNeighbourInDirection(currentDirection).doo(this);
        }
    }

    @Override
    public void accept(Replicator replicator, Set<Unit> deleteUnits) {
        deleteUnits.add(this);
        deleteUnits.add(replicator);
        replicator.kill();
        this.kill();
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
    	
    	if (dead == false) elet = "�l";
		else elet = "halott";

        String pos;
        if(currentField != null){
            pos = "(" + currentField.getPosition().x + "," + currentField.getPosition().y + ")";
        } else {
            pos = "(null,null)";
        }
    	
    	return "lövedék:" + pos + " ; mozgás cselekvést akar végrehajtani, " + irany + " irányba néz, " + szin + ", " + elet;
    }

}