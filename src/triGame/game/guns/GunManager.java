package triGame.game.guns;


import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.ManagerController;
import tSquare.system.PeripheralInput;
import triGame.game.ManagerService;
import triGame.game.entities.Person;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.UserInterface;
import triGame.game.ui.arsenal.ArsenalItem;

public class GunManager implements GameIntegratable{
	private static final int shootKey = KeyEvent.VK_SPACE;
	private final ArrayList<AbstractGun> guns = new ArrayList<AbstractGun>();
	private final ShopManager shop;
	private final PeripheralInput.Keyboard keyboard;
	private final ManagerService managers;
	
	private long lastShot = 0;
	private boolean shootPressedLast = false;
	private AbstractGun selectedGun;
	
	public GunManager(ManagerController managerController,
			ManagerService managers, ShopManager shop,
			PeripheralInput.Keyboard keyboard) {
		
		this.managers = managers;
		this.shop = shop;
		this.keyboard = keyboard;
		
		AbstractGun pistol = new GunPistol();
		shop.purchase(pistol.unlock);
		addGun(pistol);
		addGun(new GunShotgun());
		addGun(new GunSub());
	}
	
	@Override
	public void performLogic(int frameDelta) {
		userShoot();
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		managers.projectile.draw(g, rect);
	}
	
	private void userShoot() {
		switchGuns();
		Person player = managers.person.getPlayer();
		if (player == null || player.removeRequested())
			return;
		
		if (keyboard.isPressed(shootKey)) {
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
	
	private void switchGuns() {
		for (AbstractGun g : guns) {
			if (keyboard.isPressed(g.keyCode) && g.keyCode != selectedGun.keyCode && !g.isLocked(shop)) {
				selectedGun = g;
			}
		}
	}
	
	void addGun(AbstractGun gun) {
		if (guns.isEmpty())
			selectedGun = gun;
		guns.add(gun);
	}
	
	public void addGunsToUI(UserInterface ui) {
		for (AbstractGun gun : guns) {
			if (gun.unlock != null) {
				ArsenalItem gunItem = new ArsenalItem(gun.unlock, gun.name, gun.upgradeManager);
				gunItem.getInfo().description = gun.description;
				ui.arsenal.panel.addToArsenal(ui.arsenal.gunGroup, gunItem);
			}
		}
	}
}
