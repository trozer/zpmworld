package zpmworld;

public class Abyss extends Field {

	public Abyss() {
		super();
	}

	@Override
	public void doo(Player player) {

		switch (player.getAction().getType()) {
			case MOVE:
				player.step(this);
				player.kill();
				break;
			default:
				break;
		}
	}

	public void doo(Replicator replicator) {

		switch (replicator.getAction().getType()) {
			case MOVE:
				replicator.step(this);
				replicator.replaceField();
				break;
			default:
				break;
		}
	}

	@Override
	public void doo(Bullet bullet) {
		//containedUnits.add(bullet);
		bullet.step(this);
	}

	@Override
	public Field getNeighbourInDirection(Direction dir) {
		// TODO Auto-generated method stub
		return super.getNeighbourInDirection(dir);
	}

	@Override
	public boolean addUnit(Unit unit) {
		// TODO Auto-generated method stub
		//containedUnits.add(unit);
		unit.kill();
		return true;
	}

	@Override
	public void removeUnit() {
		// TODO Auto-generated method stub
		super.removeUnit();
	}

	@Override
	public void addNeighbour(Direction direction, Field neighbour) {
		// TODO Auto-generated method stub
		super.addNeighbour(direction, neighbour);
	}

	@Override
	public String toString(){
		return "szakadék: " + super.toString();
	}
}
