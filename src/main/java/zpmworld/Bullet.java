package zpmworld;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.Color;
import java.util.HashMap;
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
            Field target = currentField.getNeighbourInDirection(currentDirection);
            if(target != null){
                target.doo(this);
            } else {
                this.kill();
            }
        } else { //ez csak biztosíték, elméletileg soha nem fut le
            this.nextAction = new Action(ActionType.MOVE, currentDirection, color);
            Field target = currentField.getNeighbourInDirection(currentDirection);
            if(target != null){
                target.doo(this);
            } else {
                this.kill();
            }
        }
    }

    @Override
    public void accept(Replicator replicator, Set<Unit> deleteUnits) {
        deleteUnits.add(this);
        deleteUnits.add(replicator);
        replicator.kill();
        this.kill();
    }

    @Override
    public Element getXmlElement(Document doc) {
        int row;
        int col;
        if(this.getCurrentField().getPosition() == null){
            return null;
        } else {
            col = this.getCurrentField().getPosition().x;
            row = this.getCurrentField().getPosition().y;
        }

        Element unitElement = doc.createElement("unit");
        Attr attrType = doc.createAttribute("row");
        attrType.setValue(String.valueOf(row));
        unitElement.setAttributeNode(attrType);

        attrType = doc.createAttribute("col");
        attrType.setValue(String.valueOf(col));
        unitElement.setAttributeNode(attrType);

        attrType = doc.createAttribute("direction");
        String dir = "";
        switch (this.currentDirection){
            case EAST:
                dir = "E";
                break;
            case NORTH:
                dir = "N";
                break;
            case WEST:
                dir = "W";
                break;
            case SOUTH:
                dir = "S";
                break;
            default:
                dir = "NONE";
                break;
        }
        attrType.setValue(dir);
        unitElement.setAttributeNode(attrType);

        attrType = doc.createAttribute("color");
        if(color == Color.BLUE){
            attrType.setValue("blue");
        } else if (color == Color.YELLOW){
            attrType.setValue("yellow");
        } else if (color == Color.RED){
            attrType.setValue("red");
        } else if (color == Color.GREEN) {
            attrType.setValue("green");
        } else {
            return null;
        }

        unitElement.setAttributeNode(attrType);
        unitElement.appendChild(doc.createTextNode("Bullet"));

        return unitElement;
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