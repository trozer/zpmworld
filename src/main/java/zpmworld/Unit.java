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
	
	//Az egységek lövedékkel való kontaktusát szabályozó metódus, mely alapértelmezetten a lövedék sikeres mezorelépését valósítja meg
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
