package triGame.game.guns;


import java.awt.event.KeyEvent;

import tSquare.util.Observer;
import triGame.game.entities.Person;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class GunPistol extends AbstractGun {
	private static final int shotDelayDelta = -20;
	private static final int initialDamage = -35;
	private static final int damageDelta = -10;
	private static final int initialSpeed = 800;
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	
	public GunPistol() {
		super(KeyEvent.VK_1, //key to switch to gun
				"Pistol",    //name
				"Click to choose upgrades.",     //description
				new ShopItem("Pistol", 0, true), //to buy the gun
				230,         //semi-shot delay
				false,       //full auto
				0);          //auto-shot delay
		
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 100), 3, semiShotDelay, shotDelayDelta);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 100), 3, initialDamage, damageDelta);
		upgradeManager.addUpgrade(fireRateUpgrade);
		upgradeManager.addUpgrade(damageUpgrade);
		upgradeManager.observer().watch(fireRateObserve);
	}
	
	@Override
	public void shoot(Person player, ProjectileManager projManager) {
		int x = (int) player.getCenterX();
		int y = (int) player.getCenterY();
		double angle = player.getAngle();
		
		projManager.create(x, y, angle, initialSpeed, damageUpgrade.getValue());
	}
	
	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = t.getValue();
			}
		}
	};

}
