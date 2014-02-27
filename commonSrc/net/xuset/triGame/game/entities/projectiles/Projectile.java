package net.xuset.triGame.game.entities.projectiles;

import net.xuset.objectIO.netObject.NetVar;
import net.xuset.objectIO.netObject.ObjControllerI;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.system.sound.ISound;
import net.xuset.tSquare.system.sound.SoundStore;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.zombies.Zombie;


public class Projectile extends Entity {
	public static final String SPRITE_ID = "projectile";
	public static final String SOUND_ID = "media/Pistol_Shot.wav";
	
	private final ISound fireSound = SoundStore.get(SOUND_ID);

	protected final GameGrid gameGrid;
	protected final ManagerService managers;
	protected NetVar.nDouble speed;
	protected NetVar.nInt damage;
	protected NetVar.nBool noBuildingCollisions;

	protected Projectile(String sSpriteId, double startX, double startY,
			double angle, double speed, int damage, boolean noBuildingCollisions,
			ManagerService managers, GameGrid gameGrid) {
		
		super(sSpriteId, startX, startY);
		this.speed.set(speed);
		this.damage.set(damage);
		this.noBuildingCollisions.set(noBuildingCollisions);
		this.managers = managers;
		this.gameGrid = gameGrid;
		setAngle(angle);
		fireSound.play();
	}
	
	protected Projectile(GameGrid gameGrid, EntityKey key) {
		super(key);
		this.gameGrid = gameGrid;
		managers = null;
	}

	@Override
	public void update(int frameDelta) {
		double dist = speed.get() * frameDelta / 1000.0;
		moveForward(dist);
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
		
		IPointR center = new Point(getCenterX(), getCenterY());
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
		boolean isOutside = (getX() < 0 || getX() >= gameGrid.getGridWidth() || getY() < 0 || getY() >= gameGrid.getGridHeight());
		
		if (isOutside)
			remove();
		return isOutside;
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		speed = new NetVar.nDouble(0.0, "speed", objClass);
		damage = new NetVar.nInt(0, "damage", objClass);
		noBuildingCollisions = new NetVar.nBool(true, "noBuildingCollisions", objClass);
	}
}
