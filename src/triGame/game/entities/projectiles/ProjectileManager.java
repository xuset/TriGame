package triGame.game.entities.projectiles;


import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import triGame.game.ManagerService;
import triGame.game.entities.projectiles.ProjectileCreator.ICreate;

public class ProjectileManager extends Manager<Projectile> {
	public static final String HASH_MAP_KEY = "projectile";
	
	private final ProjectileCreator creator;
	private final ProjectileCreator mortorCreator;
	private final ManagerService managers;

	public ProjectileManager(ManagerController controller, ManagerService managers) {
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		creator = new ProjectileCreator(controller.creator, "proj", standardCreate);
		mortorCreator = new ProjectileCreator(controller.creator, "mortProj", mortorCreate);
		creator.allowUpdates = false;
	}
	
	public Projectile create(int x, int y, double angle, int speed, int damage) {
		Projectile p = creator.create(Projectile.SPRITE_ID, x, y, angle, speed, damage, false, this);
		return p;
	}
	
	public Projectile towerCreate(int x, int y, double angle, int speed, int damage) {
		Projectile p = creator.create(Projectile.SPRITE_ID, x, y, angle, speed, damage, true, this);
		return p;
	}
	
	public Projectile mortorCreate(int x, int y, double angle, int speed, int damage) {
		Projectile p = mortorCreator.create(Projectile.SPRITE_ID, x, y, angle, speed, damage, true, this);
		return p;
	}
	
	private final ProjectileCreator.ICreate standardCreate = new ICreate() {
		@Override
		public Projectile create(String spriteId, int x, int y, double angle,
				int speed, int damage, boolean noBCollisions, EntityKey key) {
			return new Projectile(spriteId, x, y, angle, speed, damage, noBCollisions, managers, key);
		}
	};
	
	private final ProjectileCreator.ICreate mortorCreate = new ICreate() {
		@Override
		public Projectile create(String spriteId, int x, int y, double angle,
				int speed, int damage, boolean noBCollisions, EntityKey key) {
			return null;
		}
	};

}
