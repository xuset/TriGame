package net.xuset.triGame.game.guns;

import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;


public class GunSub extends AbstractGun {
	private static final int shotDelayDelta = -10;
	private static final int initialDamage = -20;
	private static final int damageDelta = -10;
	private static final int initialSpeed = 18;
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	
	public GunSub() {
		super(GunType.SUB,
				"Sub",       //name
				"A rapid fire sub-machine gun to defend against evil red triangles. Keyboard #3.", //description
				new ShopItem("Sub", 200, true), //to buy the gun
				180,         //semi-shot delay
				true,        //full auto
				180);        //auto-shot delay
		
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
		
		projManager.create(x, y, angle, initialSpeed, (int) damageUpgrade.getValue(), false);
	}

	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = (int) t.getValue();
				autoShotDelay = (int) t.getValue();
			}
		}
	};
}
