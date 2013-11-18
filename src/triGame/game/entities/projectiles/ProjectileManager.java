package triGame.game.entities.projectiles;


import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import triGame.game.ManagerService;
import triGame.game.entities.projectiles.ProjectileCreator.ICreate;
import triGame.game.shopping.ShopManager;

public class ProjectileManager extends Manager<Projectile> {
	public static final String HASH_MAP_KEY = "projectile";
	
	private final ProjectileCreator creator;
	private final ManagerService managers;
	private final ShopManager shop;

	public ProjectileManager(ManagerController controller, ShopManager shop, ManagerService managers) {
		super(controller, HASH_MAP_KEY);
		this.managers = managers;
		this.shop = shop;
		creator = new ProjectileCreator(controller.creator, standardCreate);
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
	
	private final ProjectileCreator.ICreate standardCreate = new ICreate() {
		@Override
		public Projectile create(String spriteId, int x, int y, double angle,
				int speed, int damage, boolean noBCollisions, EntityKey key) {
			return new Projectile(spriteId, x, y, angle, speed, damage, noBCollisions, shop, managers, key);
		}
	};

}
