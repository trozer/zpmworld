package zpmworld;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class PortalWall extends Wall {

	private Portal portal;

	public PortalWall() {
		super();
	}

	public PortalWall(Point position) {
		super(position);
	}


	public PortalWall(Portal portal) {
		super();
		this.portal = portal;
	}

	public PortalWall(Portal portal, Point position) {
		super(position);
		this.portal = portal;
	}

	public Portal getPortal() {
		return portal;
	}

	public void setPortal(Portal portal) {
		this.portal = portal;
	}

	public Color getColor(){
		if(portal != null){
			return portal.getColor(this);
		} else {
			return null;
		}
	}

	@Override
	public void doo(Player player) {
		switch (player.getAction().getType()) {
			case MOVE:
				if (portal.amIPortal(this)) {
					Field pair = portal.getPair(this);

					if(pair == null){
						return;
					}
					if(!pair.checkAcceptance(player,ActionType.MOVE)){
						return;
					}

					player.step(this);
					containedUnits.add(player);

					// Speciális cselekvések: ez ide nem is feltétlenül kell (játék szabályai miatt soha nem kell)
					if (!containedUnits.isEmpty()){
						Set<Unit> deleteUnits = new HashSet<Unit>();
						for(Unit unit : containedUnits){
							unit.accept(player,deleteUnits);
						}
						//takarítás
						for(Unit unit : deleteUnits){
							if(containedUnits.contains(unit)){
								containedUnits.remove(unit);
							}
						}
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
	public String toString(){
		if(portal.amIPortal(this))
			return "port�lfal: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, " 
				+ portal.getColor(this).toString() + "sz�n� port�l van rajta, "
				+ containedUnits.size() + "darab tárolt egys�g";
		else
			return "port�lfal: (" + (int)(position.getX()) + "," + (int)(position.getY()) + ") poz�ci�, " 
			+ "nincs rajta port�l, " 
			+ containedUnits.size() + "darab t�rolt egys�g";
	}

	/*@Override
	public String toString() {
		return "PortalWall(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() +
				") ; containedUnits: " + containedUnits.size() + "db ; portal: " + (portal == null ? "nincs " : ("van : " + portal.getColor(this)));
	}*/
}
