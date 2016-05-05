package zpmworld;

import java.awt.Point;

public class Wall extends Field {

	public Wall() {
		super();
	}

	public Wall(Point position){
		super(position);
	}

	@Override
	public void doo(Bullet bullet) { //TODO hibakezel�s.. de az kb mindenhova k�ne
		bullet.step(this);
		bullet.kill();
	}

	@Override
	public void addUnit(Unit unit) {
		return;
	}

	/*@Override
	public String toString(){
		return "fal: " + super.toString();
	}*/

	@Override
	public String toString() {
		return "Wall(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " + containedUnits.size() + "db ";
	}

}
