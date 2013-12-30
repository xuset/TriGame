package triGame.game.entities.projectiles;

import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.math.Point;
import tSquare.system.Sound;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.zombies.Zombie;
import triGame.game.entities.buildings.Building;

public class Projectile extends Entity {
	public static final String SPRITE_ID = "projectile";
	public static final String SOUND_ID = "media/Pistol_Shot.wav";
	
	private final Sound fireSound = Sound.get(SOUND_ID);

	protected final ManagerService managers;
	protected NetVar.nInt speed, damage;
	protected NetVar.nBool noBuildingCollisions;

	protected Projectile(String sSpriteId, double startX, double startY,
			double angle, int speed, int damage, boolean noBuildingCollisions,
			ManagerService managers) {
		
		super(sSpriteId, startX, startY);
		this.speed.set(speed);
		this.damage.set(damage);
		this.noBuildingCollisions.set(noBuildingCollisions);
		this.managers = managers;
		setAngle(angle);
		fireSound.play();
	}
	
	protected Projectile(EntityKey key) {
		super(key);
		managers = null;
	}

	@Override
	public void performLogic(int frameDelta) {
		moveForward(speed.get() * frameDelta / 1000.0);
		boolean removed = checkBounds();
		
		if (!removed && owned()) {
			if (!checkZombieCollision()) {
				checkBuildingCollision();
			}
		}
	}
	
	protected boolean checkBuildingCollision() {
		if (noBuildingCollisions.get())
			return false;
		
		Point center = new Point(getCenterX(), getCenterY());
		Params.roundToGrid(center);
		if (!managers.building.objectGrid.isBlockOpen(center)) {
			Building b = collidedWithFirst(managers.building.list);
			if (b != null) {
				handleBuildingCollision(b);
				return true;
			}
		}
		return false;
	}
	
	protected boolean checkZombieCollision() {
		Zombie z = collidedWithFirst(managers.zombie.list);
		if (z != null) {
			handleZombieCollision(z);
			return true;
		}
		return false;
	}
	
	protected void handleBuildingCollision(Building b) {
		b.modifyHealth(damage.get());
		remove();
	}
	
	protected void handleZombieCollision(Zombie z) {
		remove();
 		z.hitByProjectile(damage.get());
	}
	
	protected boolean checkBounds() { 
		boolean isOutside = (getX() < 0 || getX() >= Params.GAME_WIDTH ||
				getY() < 0 || getY() >= Params.GAME_HEIGHT);
		
		if (isOutside)
			remove();
		return isOutside;
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		speed = new NetVar.nInt(0, "speed", objClass);
		damage = new NetVar.nInt(0, "damage", objClass);
		noBuildingCollisions = new NetVar.nBool(true, "noBuildingCollisions", objClass);
	}
}
