package triGame.game.guns;


import java.awt.event.KeyEvent;

import tSquare.util.Observer;
import triGame.game.entities.Person;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeItem;

public class GunShotgun extends AbstractGun {
	private static final int initialSpeed = 700;
	private static final int initialDamage = -20;
	private static final int initialRange = 10; //in degrees
	
	private final UpgradeItem fireRateUpgrade;
	private final UpgradeItem damageUpgrade;
	private final UpgradeItem rangeUpgrade;
	
	public GunShotgun() {
		super(KeyEvent.VK_2, //key to switch to gun
				"Shotgun",    //name
				"A 3-round burst should it. Keybord #2.",     //description
				new ShopItem("Shotgun", 200, true), //to buy the gun
				500,         //semi-shot delay
				true,       //full auto
				700);          //auto-shot delay
		
		
		fireRateUpgrade = new UpgradeItem(new ShopItem("Fire rate", 50), 3, autoShotDelay, -50);
		damageUpgrade = new UpgradeItem(new ShopItem("Damage", 50), 3, initialDamage, -10);
		rangeUpgrade = new UpgradeItem(new ShopItem("Range", 50), 3, initialRange, -2);
		upgradeManager.addUpgrade(fireRateUpgrade);
		upgradeManager.addUpgrade(damageUpgrade);
		upgradeManager.addUpgrade(rangeUpgrade);
		upgradeManager.observer().watch(fireRateObserve);
	}
	
	@Override
	protected void shoot(Person player, ProjectileManager projManager) {
		int x = (int) player.getCenterX();
		int y = (int) player.getCenterY();
		double deltaAngle = rangeUpgrade.getValue();
		int bursts = 3;
		
		for (int i = 0; i < bursts; i++) {
			double relativeAngle = deltaAngle * (i + 1 - (bursts + 1) / 2.0);
			double angle = player.getAngle() + relativeAngle;
			projManager.create(x, y, angle, initialSpeed, damageUpgrade.getValue(), false);
		}
	}
	
	private Observer.Change<UpgradeItem> fireRateObserve = new Observer.Change<UpgradeItem>() {
		@Override
		public void observeChange(UpgradeItem t) {
			if (t == fireRateUpgrade) {
				semiShotDelay = t.getValue() - 200;
				autoShotDelay = t.getValue();
			}
		}
	};
}
