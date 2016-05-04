package zpmworld;
import java.awt.Point;

public class Abyss extends Field {

	// Konstruktorok
	public Abyss() {
		super();
	}

	public Abyss(Point position) {
		super(position);
	}

	// M�k�d�st befoly�sol� met�dusok

	/**
	 * Abyss eset�n a Player MOVE �s DROP is mindig siker�l, de m�s nem �s nincs a playerrel kapcsolatos speci�lis
	 * cselekv�s (ha bullet k�pes meg�lni player, ha az nekimegy, akkor lehetne, de �gy j�tszhat�bb (player �gyis meghal))
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
	 * Bullet eset�n az acceptre b�zzuk az �tk�z�s kezel�s�t, ha van containedUnit (ami itt csak
	 * egy m�sik Bullet lehet). Cselekv�st nem vizsg�lunk, mert a Bullet csak MOVE-t tud.
	 * @param bullet
	 */
	@Override
	public void doo(Bullet bullet) {
		if(!checkAcceptance(bullet, ActionType.MOVE)){
			return;
		}

		bullet.step(this);
		containedUnits.add(bullet);

		if(!containedUnits.isEmpty()){
			for(Unit unit : containedUnits){
				unit.accept(bullet, this);
			}
		}
	}

	/**
	 * Replik�tor eset�n mindig siker�l a r�mozg�s: m�g ha van is rajta bullet �ppen, akkor is a replik�tor speci�lis
	 * viselked�se el�nyt �lvez, �gy a bullet nem sz�nik meg, de az abyss �talakul (�gy jobban j�tszhat�).
	 * @param replicator
     */
	@Override
	public void doo(Replicator replicator) {
		if(!checkAcceptance(replicator, ActionType.MOVE)){	//ez nem is felt�tlen kell, a j�t�k szab�lyai miatt
			return;
		}

		replicator.step(this);
		replicator.replaceField();
	}



	@Override
	public String toString(){
		return "szakad�k: " + super.toString();
	}
}
