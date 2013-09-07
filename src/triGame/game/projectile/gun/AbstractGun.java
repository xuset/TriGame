package triGame.game.projectile.gun;

import triGame.game.entities.Person;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.Upgradable;
import triGame.game.shopping.UpgradeManager;

public abstract class AbstractGun implements Upgradable{
	private GunManager gunManager;
	private UpgradeManager upgradeManager;
	private ShopItem unlock = null;
	
	public boolean automaticFire = true;
	public int autoShotDelay = 0;
	public int semiShotDelay = 0;
	public int keyCode;
	public String name = "";
	
	public abstract void shoot(Person player, ProjectileContainer container);
	
	public UpgradeManager getUpgradeManager() { return upgradeManager; }
	public ShopItem getUnlockItem() { return unlock; }
	protected GunManager getGunManager() { return gunManager; }
	
	public AbstractGun(GunManager gunManager, int keyCode, String name) {
		this.gunManager = gunManager;
		this.gunManager.addGun(this);
		this.keyCode = keyCode;
		this.name = name;
	}
	
	public boolean isShopAble() {
		return upgradeManager != null || unlock != null;
	}
	
	public void setUpgradeManager(UpgradeManager upgradeManager) {
		this.upgradeManager = upgradeManager;
	}
	
	public void setLocked(ShopItem item) {
		this.unlock = item;
	}
	
	public boolean unlock() {
		ShopManager shop = gunManager.getShopManager();
		if (shop.hasPurchased(unlock) == false)
			return shop.purchase(unlock);
		return false;
	}
	
	public boolean isLocked() {
		if (unlock != null) {
			if (gunManager.getShopManager().hasPurchased(unlock))
				return false;
		} else
			return false;
		return true;
	}
}
