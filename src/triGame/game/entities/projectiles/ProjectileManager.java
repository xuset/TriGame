package triGame.game.entities.projectiles;


import tSquare.game.entity.Creator;
import tSquare.game.entity.Creator.CreateFunc;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import triGame.game.ManagerService;

public class ProjectileManager extends Manager<Projectile> {
	public static final String HASH_MAP_KEY = "projectile";
	
	private final Creator<Projectile> creator;
	private final Creator<Projectile> mortarCreator;
	private final ManagerService managers;

	public ProjectileManager(ManagerController controller, ManagerService managers) {
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		creator = new Creator<Projectile>("proj", controller.creator, new ProjectileCreate());
		mortarCreator = new Creator<Projectile>("mortarProj", controller.creator, new MortarCreate());
		updatesAllowed = false;
	}
	
	public Projectile create(int x, int y, double angle, int speed, int damage, boolean noBuildingCollisions) {
		Projectile p = new Projectile(Projectile.SPRITE_ID, x, y, angle, speed, damage, noBuildingCollisions, managers);
		add(p);
		creator.createOnNetwork(p, this);
		return p;
	}
	
	public Projectile mortarCreate(int x, int y, double angle, int speed, int damage) {
		Projectile p = new MortarProjectile(x, y, angle, speed, damage, managers);
		add(p);
		mortarCreator.createOnNetwork(p, this);
		return p;
	}
	
	private class ProjectileCreate implements CreateFunc<Projectile> {
		@Override public Projectile create(EntityKey key) {
			return new Projectile(key);
		}
	}
	
	private class MortarCreate implements CreateFunc<Projectile> {
		@Override public Projectile create(EntityKey key) {
			return new MortarProjectile(key);
		}
	}
}
