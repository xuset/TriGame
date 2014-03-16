package net.xuset.triGame.game.guns;

import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class GunPistol extends AbstractGun {
	public static final String SOUND_ID = "media/Pistol_Shot.wav";
	
	private static final int shotDelayDelta = -20;
	private static final int initialDamage = -35;
	private static final int damageDelta = -8;
	private static final int initialSpeed = 16;
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	
	public GunPistol() {
		super(GunType.PISTOL,
				"Pistol",    //name
				"Click to choose upgrades.",     //description
				new ShopItem("Pistol", 0, true), //to buy the gun
				230,         //semi-shot delay
				false,       //full auto
				0);          //auto-shot delay
		
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 50), 3, semiShotDelay, shotDelayDelta);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 50), 3, initialDamage, damageDelta);
		upgradeManager.addUpgrade(fireRateUpgrade);
		upgradeManager.addUpgrade(damageUpgrade);
		upgradeManager.observer().watch(fireRateObserve);
	}
	
	@Override
	public void shoot(Person player, ProjectileManager projManager) {
		double x = player.getCenterX();
		double y = player.getCenterY();
		double angle = player.getAngle();
		
		projManager.create(x, y, angle, initialSpeed, (int) damageUpgrade.getValue(),
				false, SOUND_ID);
	}
	
	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = (int) t.getValue();
			}
		}
	};

}
