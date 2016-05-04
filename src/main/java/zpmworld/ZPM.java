package zpmworld;

import java.util.HashMap;
import java.util.Map;

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
        playerAccept.put(ActionType.DROP,false);    //olvashat�s�g miatt van k�l�n kiemelve, de ha nem lenne itt, akkor is false lenne
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
     * Az accept csak azt a cselekv�st v�gzi el, ami �t mag�t a Fieldr�l a kezdem�nyez� fel� mozgatja. Az addZPM mindig
     * siker�lt.
     * @param launcher
     * @param target
     */
    @Override
    public void accept(Player target, Field launcher) {
        switch (target.getAction().getType()){
            case GRAB:
                launcher.removeUnit(this);
                currentField = null;
                target.addZPM(this);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString(){
    	return "ZPM";
    }
}