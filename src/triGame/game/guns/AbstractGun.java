package triGame.game.guns;

import triGame.game.entities.Person;
import triGame.game.entities.projectiles.ProjectileManager;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;

public abstract class AbstractGun{
	protected final ShopItem unlock;
	protected final int keyCode;
	protected final String name;
	protected final String description;
	protected final UpgradeManager upgradeManager = new UpgradeManager();
	protected boolean automaticFire = true;
	protected int autoShotDelay = 0;
	protected int semiShotDelay = 0;
	
	protected abstract void shoot(Person player, ProjectileManager projManager);
	
	public AbstractGun(int keyCode, String name, String description, ShopItem unlock,
			int semiShotDelay, boolean automaticFire, int autoShotDelay) {
		
		this.keyCode = keyCode;
		this.name = (name == null) ? "" : name;
		this.description = (description == null || description == "") ? "-" : description;
		this.unlock = unlock;
		this.semiShotDelay = semiShotDelay;
		this.automaticFire = automaticFire;
		this.autoShotDelay = autoShotDelay;
	}
	
	public boolean isLocked(ShopManager shop) {
		if (unlock != null) {
			if (shop.hasPurchased(unlock))
				return false;
		} else
			return false;
		return true;
	}
}
