package triGame.game.projectile;

import triGame.game.entities.building.Tower;

public class ProjectileTower extends ProjectilePistol {
	public static final String IDENTIFIER = "tower";
	private static final int initialSpeed = 800;
	private static final int initialDamage = -30;
	private ProjectileTower(int x, int y, double theta, ProjectileContainer container, long ownerId) {
		super(x, y, theta, container, ownerId);
		this.speed = initialSpeed;
		this.damage = initialDamage;
	}
	
	public static ProjectileTower create(Tower start, ProjectileContainer container) {
		ProjectileTower p = new ProjectileTower((int) start.getCenterX(), (int) start.getCenterY(), start.getAngle(), container, container.getUserId());
		container.getCreator().createOnNetwork(p);
		container.add(p);
		return p;
	}
	
	public static ProjectileTower createFromString(String parameters, ProjectileContainer container, long ownerId) {
		String[] parameter = parameters.split(":");
		int x = Integer.parseInt(parameter[0]);
		int y = Integer.parseInt(parameter[1]);
		double theta = Double.parseDouble(parameter[2]);
		int speed = Integer.parseInt(parameter[3]);
		ProjectileTower p = new ProjectileTower(x, y, theta, container, ownerId);
		p.speed = speed;
		container.add(p);
		return p;
	}
	
	public void performLogic() {
		entity.moveForward(speed * container.getDelta() / 1000);
		if (!CollisionHandling.doEdgeCollision(entity, this, container)) {
			CollisionHandling.doZombieCollision(entity, damage, this, container);
		}
	}

}
