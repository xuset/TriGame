package triGame.game.entities.zombies;

import java.awt.Graphics2D;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import triGame.game.ManagerService;
import triGame.game.entities.HealthBar;
import triGame.game.shopping.ShopManager;

public class BossZombie extends Zombie {
	public static final String SPRITE_ID = "bossZombie";
	
	private final HealthBar healthBar;
	private final NetVar.nInt maxHealth;

	public BossZombie(double x, double y, ManagerService managers, ZombieHandler zombieHandler,
			boolean isServer, ZombiePathFinder pathFinder, ShopManager shop, EntityKey key) {
		
		super(SPRITE_ID, x, y, managers, zombieHandler, isServer, pathFinder, shop, key);
		healthBar = new HealthBar(this);
		maxHealth = new NetVar.nInt(100, "maxHealth", objClass);
		maxHealth.event = new NetVar.OnChange<Integer>() {
			@Override
			public void onChange(NetVar<Integer> var, Connection c) {
				healthBar.maxHealth = var.get();
			}
		};
	}
	
	@Override
	void setMaxHealth(int max) {
		int actual = (int) (max - getHealth());
		maxHealth.set(max);
		modifyHealth(actual);
		healthBar.maxHealth = max;
	}

	@Override
	public void hitByProjectile(int damage) {
		modifyHealth(damage);
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		super.draw(g, rect);
		healthBar.draw(g, rect);
	}

}
