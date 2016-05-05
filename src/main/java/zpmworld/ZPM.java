package zpmworld;

import java.util.*;

public class ZPM extends Unit{

    public ZPM(Field currentField) {
        super(currentField, 0);
    }

    public ZPM(Field currentField, int weight){
        super(currentField, weight);
    }

    @Override
    protected Map<Class, Map<ActionType, Boolean>> initAcceptance() {
        Map<Class,Map<ActionType,Boolean>> returnMap = new HashMap<Class, Map<ActionType, Boolean>>();
        Map<ActionType, Boolean> playerAccept = new HashMap<ActionType, Boolean>();
        playerAccept.put(ActionType.MOVE,true);
        playerAccept.put(ActionType.GRAB,true);
        playerAccept.put(ActionType.DROP,false);    //olvashatóság miatt van külön kiemelve, de ha nem lenne itt, akkor is false lenne
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

    /**
     * Az accept csak azt a cselekvést végzi el, ami õt magát a Fieldrõl a kezdeményezõ felé mozgatja. Az addZPM mindig
     * sikerült.
     * @param target
     */
    @Override
    public void accept(Player target, Set<Unit> deleteUnits) {
        switch (target.getAction().getType()){
            case MOVE:
            case GRAB:
                deleteUnits.add(this);
                currentField = null;
                target.addZPM(this);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString(){
        String pos;
        if(currentField != null){
            pos = "(" + currentField.getPosition().x + "," + currentField.getPosition().y + ")";
        } else {
            pos = "(null,null)";
        }
    	return "ZPM " + pos;
    }
}