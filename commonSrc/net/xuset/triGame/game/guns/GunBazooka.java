package net.xuset.triGame.game.guns;

import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.UpgradeItem;

public class GunBazooka extends AbstractGun {
	private static final double speed = 8.0, maxDistance = 7.0;
	private static final double fireRateDelta = -50, damageDelta = -20;//, splashDelta = 0.2;
	private static final double initFireRate = 800, initDamage = -150, initSplash = 1.0;
	
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	//private final UpgradeItem splashUpgrade;

	public GunBazooka() {
		
		super(
				GunType.BAZOOKA,
				"Bazooka",
				"When bullets arent enough, explosives are.",
				new ShopItem("Bazooka", 400, true),
				(int) initFireRate,         //semi-shot delay
				false,       //full auto
				0);          //auto-shot delay
		
		fireRateUpgrade = new UpgradeItem(
				new ShopItem("Fire rate", 100), 3, semiShotDelay, fireRateDelta);
		damageUpgrade = new UpgradeItem(
				new ShopItem("Damage", 100), 3, initDamage, damageDelta);
		//splashUpgrade = new UpgradeItem(
				//new ShopItem("Spash range", 100), 3, initSplash, splashDelta);
		
		upgradeManager.addUpgrade(fireRateUpgrade);
		upgradeManager.addUpgrade(damageUpgrade);
		//upgradeManager.addUpgrade(splashUpgrade);
		
		upgradeManager.observer().watch(fireRateObserve);
	}

	@Override
	protected void shoot(Person player, ProjectileManager projManager) {
		double x = player.getCenterX();
		double y = player.getCenterY();
		double angle = player.getAngle();
		double damage = damageUpgrade.getValue();
		double splashRadius = initSplash;//splashUpgrade.getValue();
		
		projManager.mortarCreate(x, y, angle, speed,
				(int) damage, splashRadius, maxDistance, false);
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
