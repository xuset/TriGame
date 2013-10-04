package triGame.game.projectile;

import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;
import tSquare.paths.ObjectGrid;
import triGame.game.entities.zombies.Zombie;

public abstract class Projectile implements GameIntegratable{
	private final static int hitBackDistance = 10;
	
	protected ProjectileContainer container;
	protected long ownerId;
	
	public abstract String getIdentifier();
	public abstract String createToString();
	
	protected Projectile(ProjectileContainer container, long ownerId) {
		this.container = container;
		this.ownerId = ownerId;
	}
	
	protected Entity checkBuildingCollision(Entity entity) {
		ObjectGrid bgrid = container.buildingManager.objectGrid;
		ObjectGrid wgrid = container.wallManager.objectGrid;
		if (!bgrid.isBlockOpen(bgrid.roundToGrid(entity.getCenter())))
			return entity.collidedWithFirst(container.buildingManager.getList());
		if (!wgrid.isBlockOpen(wgrid.roundToGrid(entity.getCenter())))
			return entity.collidedWithFirst(container.wallManager.getList());
		return null;
	}
	
	protected Zombie checkZombieCollision(Entity entitie) {
		return entitie.collidedWithFirst(container.zombieManager.getList());
	}
	
	protected void moveHitEntity(Entity e) {
		e.moveForward(-hitBackDistance);
	}
	
	public void remove() {
		container.remove(this);
	}
}
