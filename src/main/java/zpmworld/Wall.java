package zpmworld;

public class Wall extends Field {

	public Wall() {
		super();
	}

	@Override
	public void doo(Player player) {
	}

	@Override
	public void doo(Replicator replicator) {
	}

	@Override
	public void doo(Bullet bullet) {
		bullet.step(this);
		bullet.kill();
	}

	@Override
	public Field getNeighbourInDirection(Direction dir) {
		return super.getNeighbourInDirection(dir);
	}

	@Override
	public boolean addUnit(Unit unit) {
		return false;
	}

	@Override
	public void forceAddUnit(Unit unit){
		containedUnits.add(unit);
		unit.kill();
	}

	@Override
	public void removeUnit() {
		super.removeUnit();
	}

	@Override
	public void addNeighbour(Direction direction, Field neighbour) {
		super.addNeighbour(direction, neighbour);
	}
	
	@Override
	public String toString(){
		return "fal: " + super.toString();
	}

}
