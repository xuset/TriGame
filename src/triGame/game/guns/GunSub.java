package triGame.game.guns;


import java.awt.event.KeyEvent;

import tSquare.util.Observer;
import triGame.game.entities.Person;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class GunSub extends AbstractGun {
	private static final int shotDelayDelta = -15;
	private static final int initialDamage = -20;
	private static final int damageDelta = -10;
	private static final int initialSpeed = 900;
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	
	public GunSub() {
		super(KeyEvent.VK_3, //key to switch to gun
				"Sub",       //name
				"A rapid fire sub-machine gun to defend against evil red triangles", //description
				new ShopItem("Sub", 200, true), //to buy the gun
				180,         //semi-shot delay
				true,        //full auto
				180);        //auto-shot delay
		
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
				autoShotDelay = t.getValue();
			}
		}
	};
}
