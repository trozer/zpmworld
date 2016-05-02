package zpmworld;

import java.awt.Color;

public class PortalWall extends Wall {

	private Portal portal;

	public PortalWall() {
		super();
	}

	public PortalWall(Portal portal) {
		this.portal = portal;
	}

	public Portal getPortal() {
		return portal;
	}

	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	@Override
	public void doo(Player player) {
		switch (player.getAction().getType()) {
        case MOVE:
		if (portal.amIPortal(this)) {
			if (portal.getPair(this) != null) {
				player.step(portal.getPair(this));
				portal.getPair(this).containedUnits.add(player);
			}
		}
		break;
		default:
			break;
		}
	}

	@Override
	public void doo(Bullet bullet) {
		if (portal.amIPortal(this)) {
			bullet.step(this);
			bullet.kill();
		} else {
			portal.createPortal(this, bullet.getColor());
			bullet.step(this);
			bullet.kill();
		}
	}

	@Override
	public Field getNeighbourInDirection(Direction dir) {
		// TODO Auto-generated method stub
		return super.getNeighbourInDirection(dir);
	}

	@Override
	public boolean addUnit(Unit unit) {
		// TODO Auto-generated method stub
		return super.addUnit(unit);
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
		if(portal.amIPortal(this))
			return "portálfal: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") pozíció, " 
				+ portal.getColor(this) + "színû portál van rajta, " 
				+ containedUnits.size() + "darab tárolt egység";
		else
			return "portálfal: (" + toInt(position.getX()) + "," + toInt(position.getY()) + ") pozíció, " 
			+ "nincs rajta portál, " 
			+ containedUnits.size() + "darab tárolt egység";
	}
}
