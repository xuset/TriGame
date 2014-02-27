package net.xuset.triGame.game.entities.zombies;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.HealthBar;
import net.xuset.triGame.game.shopping.ShopManager;


public class BossZombie extends Zombie {
	public static final String SPRITE_ID = "bossZombie";
	
	private final HealthBar healthBar;
	
	BossZombie(ManagerService managers, ShopManager shop, EntityKey key) { //for clients
		super(managers, shop, key);
		healthBar = new HealthBar(this);
		healthBar.maxHealth = (int) getHealth();
	}
	
	BossZombie(double x, double y, Entity target, long spawnDelay, double speed, int buildingG, int maxHealth, 
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
	public void draw(IGraphics g) {
		super.draw(g);
		healthBar.draw(g);
	}

}
