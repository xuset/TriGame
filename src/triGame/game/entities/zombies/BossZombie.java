package triGame.game.entities.zombies;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import triGame.game.ManagerService;
import triGame.game.entities.HealthBar;
import triGame.game.shopping.ShopManager;

public class BossZombie extends Zombie {
	public static final String SPRITE_ID = "bossZombie";
	
	private final HealthBar healthBar;
	
	BossZombie(ManagerService managers, ShopManager shop, EntityKey key) { //for clients
		super(managers, shop, key);
		healthBar = new HealthBar(this);
		healthBar.maxHealth = (int) getHealth();
	}
	
	BossZombie(double x, double y, Entity target, long spawnDelay, int speed, int buildingG, int maxHealth, 
			ManagerService managers, ZombieHandler zombieHandler, boolean isServer, ZombiePathFinder pathFinder,
			ShopManager shop, EntityKey key) { //for server
		
		super(SPRITE_ID, x, y, target, spawnDelay, speed, buildingG, maxHealth,
				managers, zombieHandler, isServer, pathFinder, shop, key);
		healthBar = new HealthBar(this);
		healthBar.maxHealth = (int) getHealth();
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
