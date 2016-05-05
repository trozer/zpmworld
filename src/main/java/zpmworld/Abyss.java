package zpmworld;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Abyss extends Field {

	// Konstruktorok
	public Abyss() {
		super();
	}

	public Abyss(Point position) {
		super(position);
	}

	// Mûködést befolyásoló metódusok

	/**
	 * Abyss esetén a Player MOVE és DROP is mindig sikerül, de más nem és nincs a playerrel kapcsolatos speciális
	 * cselekvés (ha bullet képes megölni player, ha az nekimegy, akkor lehetne, de így játszhatóbb (player úgyis meghal))
	 * @param player
     */
	@Override
	public void doo(Player player) {

		ActionType actionType = player.getAction().getType();
		if(!checkAcceptance(player,actionType)){
			return;
		}

		switch (actionType) {
			case MOVE:
				player.step(this);
				player.kill();
				break;
			case DROP:
				Box tempBox = player.dropBox();
				tempBox.kill();
			default:
				break;
		}
	}

	/**
	 * Bullet esetén az acceptre bízzuk az ütközés kezelését, ha van containedUnit (ami itt csak
	 * egy másik Bullet lehet). Cselekvést nem vizsgálunk, mert a Bullet csak MOVE-t tud.
	 * @param bullet
	 */
	@Override
	public void doo(Bullet bullet) {
		if(!checkAcceptance(bullet, ActionType.MOVE)){
			return;
		}

		bullet.step(this);
		containedUnits.add(bullet);

		if (!containedUnits.isEmpty()){
			Set<Unit> deleteUnits = new HashSet<Unit>();
			for(Unit unit : containedUnits){
				unit.accept(bullet,deleteUnits);
			}
			//takarítás
			for(Unit unit : deleteUnits){
				if(containedUnits.contains(unit)){
					containedUnits.remove(unit);
				}
			}
		}
	}

	/**
	 * Replikátor esetén mindig sikerül a rámozgás: még ha van is rajta bullet éppen, akkor is a replikátor speciális
	 * viselkedése elõnyt élvez, így a bullet nem szûnik meg, de az abyss átalakul (így jobban játszható).
	 * @param replicator
     */
	@Override
	public void doo(Replicator replicator) {
		if(!checkAcceptance(replicator, ActionType.MOVE)){	//ez nem is feltétlen kell, a játék szabályai miatt
			return;
		}

		replicator.step(this);
		replicator.replaceField();
	}



	@Override
	public String toString(){
		return "Abyss(" + this.hashCode() + ") : (" + (int)position.getX() + "," + (int)position.getY() + ") ; containedUnits: " + containedUnits.size() + "db ";
	}
}
