package zpmworld;

public abstract class Unit {
	protected boolean dead = false;
	protected Field currentField;
	int weight;
	
	public void action(){}
	
	public void kill(){
		dead = true;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void accept(Player launcher, Field target){}
	
	public void accept(Field launcher, Player target){}
	
	public void accept(Unit unit, Field field){}
	
	//Az egys�gek l�ved�kkel val� kontaktus�t szab�lyoz� met�dus, mely alap�rtelmezetten a l�ved�k sikeres mezorel�p�s�t val�s�tja meg
	public void accept(Bullet launcher, Field field){
		field.forceAddUnit(launcher);
		launcher.step(field);
	}
	
	public Field getCurrentField(){
		return currentField;
	}
	
	public void setCurrentField(Field field){
		currentField = field;
	}
	
	public int getWeight(){
		return weight;
	}
	
	@Override
	public
	String toString(){
		return "UNIT VAGYOK";
	}
}
