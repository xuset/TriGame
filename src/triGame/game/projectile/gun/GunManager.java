package triGame.game.projectile.gun;


import java.awt.event.KeyEvent;
import java.util.ArrayList;

import tSquare.game.GameBoard;
import tSquare.game.GameIntegratable;
import tSquare.system.PeripheralInput;
import triGame.game.TriGame;
import triGame.game.projectile.ProjectileContainer;
import triGame.game.shopping.ShopManager;

public class GunManager implements GameIntegratable{
	private static final int shootKey = KeyEvent.VK_SPACE;
	private ArrayList<AbstractGun> guns = new ArrayList<AbstractGun>();
	private TriGame game;
	private ProjectileContainer container;
	private ShopManager shop;
	private PeripheralInput.Keyboard keyboard;
	private long lastShot = 0;
	
	public ProjectileContainer getProjectiles() { return container; }
	
	ShopManager getShopManager() { return shop; }
	
	public GunManager(TriGame game, GameBoard gameBoard) {
		this.game = game;
		this.container = new ProjectileContainer(game, gameBoard);
		this.shop = game.shop;
		this.keyboard = game.getInput().keyboard;
		new GunPistol(this);
		new GunSub(this);
		new GunShotgun(this);
	}
	
	public void performLogic() {
		userShoot();
		container.performLogic();
		container.completeListModifications();
	}
	
	public void draw() {
		container.draw();
	}
	
	private boolean shootPressedLast = false;
	private AbstractGun selectedGun;
	private void userShoot() {
		for (AbstractGun g : guns) {
			if (g.keyCode != selectedGun.keyCode && g.isLocked() == false && keyboard.isPressed(g.keyCode)) {
				selectedGun = g;
			}
		}
		if (keyboard.isPressed(shootKey)) {
			if (shootPressedLast == true) {
				if (selectedGun.automaticFire && lastShot + selectedGun.autoShotDelay <= System.currentTimeMillis()) {
					selectedGun.shoot(game.player, container);
					lastShot = System.currentTimeMillis();
				}
			} else {
				if (lastShot + selectedGun.semiShotDelay <= System.currentTimeMillis()) {
					selectedGun.shoot(game.player, container);
					lastShot = System.currentTimeMillis();
				}
			}
			shootPressedLast = true;
		} else
			shootPressedLast = false;
	}
	
	public ArrayList<AbstractGun> getShopableGuns() {
		ArrayList<AbstractGun> shopable = new ArrayList<AbstractGun>();
		for (AbstractGun g : guns) {
			if (g.isShopAble())
				shopable.add(g);
		}
		return shopable;
	}
	
	public void addGun(AbstractGun gun) {
		if (guns.isEmpty())
			selectedGun = gun;
		guns.add(gun);
	}
}
