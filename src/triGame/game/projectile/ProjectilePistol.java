package triGame.game.projectile;

import tSquare.game.entity.Entity;
import triGame.game.projectile.gun.GunPistol;
import triGame.game.shopping.UpgradeManager;


public class ProjectilePistol extends Projectile{
	public final static String SPRITE_ID = "projectile";
	public final static String IDENTIFIER = "pis";
	private static final int initialSpeed = 800;
	private static final int initialDamage = -35;
	
	private int x, y;
	private double theta;
	
	protected Entity entity;
	protected int speed = initialSpeed, damage = initialDamage; //speed pixels/sec

	protected ProjectilePistol(int x, int y, double theta, ProjectileContainer container, long ownerId) {
		super(container, ownerId);
		entity = new Entity("projectile", x, y);
		entity.setAngle(theta);
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	public static ProjectilePistol create(Entity start, ProjectileContainer container, UpgradeManager upgrade) {
		ProjectilePistol p = new ProjectilePistol((int) start.getCenterX(), (int) start.getCenterY(), start.getAngle(), container, container.getUserId());
		p.damage = initialDamage - upgrade.getUpgrade(GunPistol.DAMAGE_ITEM).upgradeCount * 10;
		container.getCreator().createOnNetwork(p);
		container.add(p);
		return p;
	}
	
	/*public static ProjectilePistol create_untimed(Entity start, ProjectileContainer container) {
		ProjectilePistol p = new ProjectilePistol((int) start.getCenterX(), (int) start.getCenterY(), start.getAngle(), container, container.getUserId());
		container.commandCreate.sendCommand(p);
		container.add(p);
		return p;
	}*/   // UNUSED - pending deletion
	
	public void performLogic() {
		entity.moveForward(speed * container.getDelta() / 1000);
		if (!CollisionHandling.doEdgeCollision(entity, this, container)) {
			if (!CollisionHandling.doZombieCollision(entity, damage, this, container)) {
				CollisionHandling.doBuildingCollision(entity, damage, this, container);
			}
		}
	}
	
	public void draw() {
		entity.draw(container.gameBoard);
	}
	
	public String getIdentifier() {
		return IDENTIFIER;
	}

	public static ProjectilePistol createFromString(String command, ProjectileContainer container, long ownerId) {
		String[] parameter = command.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		double theta = Double.parseDouble(parameter[2]);
		int speed = Integer.parseInt(parameter[3]);
		ProjectilePistol p = new ProjectilePistol(x, y, theta, container, ownerId);
		p.speed = speed;
		container.add(p);
		return p;
	}
	
	public String createToString() {
		return x + ":" + y + ":" + theta + ":" + speed;
	}
}
