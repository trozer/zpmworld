package zpmworld;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Box extends Unit{

    public Box(Field currentField) {
        super(currentField, 5);
    }

    public Box(Field currentField, int weight){
        super(currentField, weight);
    }

    @Override
    protected Map<Class, Map<ActionType, Boolean>> initAcceptance() {
        Map<Class,Map<ActionType,Boolean>> returnMap = new HashMap<Class, Map<ActionType, Boolean>>();
        Map<ActionType, Boolean> playerAccept = new HashMap<ActionType, Boolean>();
        playerAccept.put(ActionType.MOVE,false);
        playerAccept.put(ActionType.GRAB,true);
        playerAccept.put(ActionType.DROP,false);
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

    @Override
    public void accept(Player player, Set<Unit> deleteUnits) {
        switch (player.getAction().getType()){
            case GRAB:
                if(player.grabBox(this)){
                    deleteUnits.add(this);
                    this.currentField = null;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public Element getXmlElement(Document doc) {
        int row;
        int col;
        if(this.getCurrentField() == null || this.getCurrentField().getPosition() == null){
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


        unitElement.appendChild(doc.createTextNode("Box"));
        return unitElement;
    }

    @Override
    public String toString(){
    	String life;
		if (dead == false) life = "él";
		else life = "halott";

        String pos;
        if(currentField != null){
            pos = "(" + currentField.getPosition().x + "," + currentField.getPosition().y + ")";
        } else {
            pos = "(null,null)";
        }
		
    	return "Doboz " +pos + " ; " + weight + " tömegû " + life;
    }
}