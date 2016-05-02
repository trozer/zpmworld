package zpmworld;

public class ZPM extends Unit{

    public ZPM(Field currentField) {
        this.currentField = currentField;
        this.weight = 0;
    }

    @Override
    public void accept(Field launcher, Player target) {
        switch (target.getAction().getType()) {
            case GRAB:
                target.addZPM(this);
                launcher.removeUnit();
                break;
            case MOVE:
                target.addZPM(this);
                launcher.removeUnit();
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