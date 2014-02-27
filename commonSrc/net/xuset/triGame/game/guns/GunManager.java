package net.xuset.triGame.game.guns;


import java.util.ArrayList;

import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.ui.arsenal.ArsenalForm.ArsenalItemAdder;
import net.xuset.triGame.game.ui.gameInput.IGunInput;



public class GunManager implements GameIntegratable{
	
	private final ArrayList<AbstractGun> guns = new ArrayList<AbstractGun>();
	private final ShopManager shop;
	private final IGunInput gunInput;
	private final ManagerService managers;
	
	private long lastShot = 0;
	private boolean shootPressedLast = false;
	private AbstractGun selectedGun;
	
	public GunManager(ManagerService managers, ShopManager shop,
			IGunInput gunInput) {
		
		this.managers = managers;
		this.shop = shop;
		this.gunInput = gunInput;
		
		AbstractGun pistol = new GunPistol();
		shop.purchase(pistol.unlock);
		addGun(pistol);
		addGun(new GunShotgun());
		addGun(new GunSub());
	}
	
	@Override
	public void update(int frameDelta) {
		userShoot();
	}
	
	@Override
	public void draw(IGraphics g) {
		managers.projectile.draw(g);
	}
	
	private void userShoot() {
		if (gunInput.changeGunRequested())
			switchGuns(gunInput.getCurrentGunType());
		Person player = managers.person.getPlayer();
		if (player == null || player.isDead())
			return;
		
		if (gunInput.shootRequested()) {
			if (shootPressedLast) {
				if (selectedGun.automaticFire && lastShot + selectedGun.autoShotDelay <= System.currentTimeMillis()) {
					selectedGun.shoot(player, managers.projectile);
					lastShot = System.currentTimeMillis();
				}
			} else {
				if (lastShot + selectedGun.semiShotDelay <= System.currentTimeMillis()) {
					selectedGun.shoot(player, managers.projectile);
					lastShot = System.currentTimeMillis();
				}
			}
			shootPressedLast = true;
		} else
			shootPressedLast = false;
	}
	
	private void switchGuns(GunType type) {
		for (AbstractGun g : guns) {
			if (g.type == type && shop.hasPurchased(g.unlock)) {
				selectedGun = g;
				return;
			}
		}
	}
	
	void addGun(AbstractGun gun) {
		if (guns.isEmpty())
			selectedGun = gun;
		guns.add(gun);
	}
	
	public void addGunsToUI(ArsenalItemAdder itemAdder) {
		for (AbstractGun gun : guns) {
			if (gun.unlock != null) {
				//TODO eventually add in gun description
				itemAdder.addGun(gun.type, gun.name, gun.unlock, gun.upgradeManager);
			}
		}
	}
}
