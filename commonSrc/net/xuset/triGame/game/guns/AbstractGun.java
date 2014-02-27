package net.xuset.triGame.game.guns;

import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.projectiles.ProjectileManager;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeManager;

public abstract class AbstractGun{
	protected final GunType type;
	protected final ShopItem unlock;
	protected final String name;
	protected final String description;
	protected final UpgradeManager upgradeManager = new UpgradeManager();
	protected boolean automaticFire = true;
	protected int autoShotDelay = 0;
	protected int semiShotDelay = 0;
	
	protected abstract void shoot(Person player, ProjectileManager projManager);
	
	public AbstractGun(GunType type, String name, String description, ShopItem unlock,
			int semiShotDelay, boolean automaticFire, int autoShotDelay) {
		
		this.type = type;
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
