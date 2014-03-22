package net.xuset.triGame.game.entities.projectiles;


import net.xuset.tSquare.game.entity.Creator;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.entity.Creator.CreateFunc;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;


public class ProjectileManager extends Manager<Projectile> {
	public static final String HASH_MAP_KEY = "projectile";
	
	private final Creator<Projectile> creator;
	private final Creator<Projectile> mortarCreator;
	private final ManagerService managers;
	private final GameGrid gameGrid;

	public ProjectileManager(ManagerController controller, ManagerService managers,
			GameGrid gameGrid) {
		
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		this.gameGrid = gameGrid;
		creator = new Creator<Projectile>("proj", controller.creator,
				new ProjectileCreate());
		mortarCreator = new Creator<Projectile>("mortarProj", controller.creator,
				new MortarCreate());
		updatesAllowed = false;
	}
	
	public Projectile create(double x, double y, double angle, int speed, int damage,
			boolean noBuildingCollisions, String soundId) {
		
		Projectile p = new Projectile(Projectile.SPRITE_ID, x, y, angle, speed, damage,
				noBuildingCollisions, managers, gameGrid, soundId);
		add(p);
		creator.createOnNetwork(p, this);
		return p;
	}
	
	public Projectile mortarCreate(double x, double y, double angle, double speed,
			int damage, double splashRadius, double maxDistance) {
		
		Projectile p = new MortarProjectile(x, y, angle, speed, damage,
				managers, gameGrid, splashRadius, maxDistance);
		add(p);
		mortarCreator.createOnNetwork(p, this);
		return p;
	}
	
	private class ProjectileCreate implements CreateFunc<Projectile> {
		@Override public Projectile create(EntityKey key) {
			return new Projectile(gameGrid, key);
		}
	}
	
	private class MortarCreate implements CreateFunc<Projectile> {
		@Override public Projectile create(EntityKey key) {
			return new MortarProjectile(gameGrid, key);
		}
	}
}
