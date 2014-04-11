package net.xuset.triGame.game.entities.projectiles;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.zombies.Zombie;


public class MortarProjectile extends Projectile {
	public static final String SOUND_ID = "media/Mortar_Shot.wav";
	public static final String SPRITE_ID = "media/MortarProjectile.png";
	private static final double explodedTime = 300;
	private static final double blowBackTime = 100;
	private static final double blowBackRadius = 15/50.0;
	
	private boolean collided = false;
	private long explodeStarted = 0;
	private long timeCreated = 0;
	private final double maxDistance, startX, startY, splashRadius;
	
	protected MortarProjectile(double startX, double startY, double angle,
			double speed, int damage, boolean noBuildingCollsion, ManagerService managers,
			GameGrid gameGrid, double splashRadius, double maxDistance) {
		
		super(SPRITE_ID, startX, startY, angle, speed, damage, noBuildingCollsion,
				managers, gameGrid, SOUND_ID);
		this.startX = startX;
		this.startY = startY;
		this.splashRadius = splashRadius;
		this.maxDistance = maxDistance;
		timeCreated = System.currentTimeMillis();
	}
	
	protected MortarProjectile(GameGrid gameGrid, EntityKey key) {
		super(gameGrid, key);
		timeCreated = System.currentTimeMillis();
		startX = getX();
		startY = getY();
		splashRadius = 1.0; //Not used if the tower is not owned
		maxDistance = 0.0;  //not used if the tower is not owned
	}

	@Override
	public void draw(IGraphics g) {
		drawBlowBack(getCenterX(), getCenterY(), g);
		
		if (collided) {
			drawExplosion(getCenterX(), getCenterY(), g);
			if (System.currentTimeMillis() > explodeStarted + explodedTime)
				remove();
		} else {
			super.draw(g);
		}
	}
	
	@Override
	public void update(int frameDelta) {
		if (collided)
			return;
		
		double dist = speed.get() * frameDelta / 1000.0;
		moveForward(dist);
		collided = checkBounds();
		
		if (!owned())
			return;
		
		
		if (didCollide()) {
			collided = true;
			
			destroyZombies();
			if (!noBuildingCollisions.get())
				destroyBuildings();
		}
		
		if (Point.distance(getX(), getY(), startX, startY) >= maxDistance)
			collided = true;
		
		if (collided)
			explodeStarted = System.currentTimeMillis();
	}
	
	private boolean didCollide() {
		return collidedWithFirst(managers.zombie.list) != null ||
				(!noBuildingCollisions.get() && (noBuildingCollisions.get() ||
						!managers.building.objectGrid.isBlockOpen(
						getCenterX(), getCenterY())));
	}
	
	private void destroyZombies() {
		for (Zombie z : managers.zombie.list) {
			double ratio = getDamageRatio(z);
			if (ratio > 0)
				z.hitByProjectile((int) (ratio * damage.get()));
		}
	}
	
	private void destroyBuildings() {
		for (Building b : managers.building.list) {
			double ratio = getDamageRatio(b);
			if (ratio > 0)
				b.damageByProjectile(ratio * damage.get());
		}
	}
	
	private double getDamageRatio(Entity e) {
		double avgSize = (e.getWidth() + e.getHeight()) / 4;
		double distance = PointR.distance(getCenterX(), getCenterY(),
				e.getCenterX(), e.getCenterY()) - avgSize;
		return 1 - distance / splashRadius;
		
	}
	
	private void drawBlowBack(double drawX, double drawY, IGraphics g) {
		final double blowBackRatio = (System.currentTimeMillis() - timeCreated) / blowBackTime;
		final double radius = blowBackRatio * blowBackRadius;
		if (blowBackRatio < 1 && blowBackRatio > 0)
			drawCircle(drawX, drawY, radius, TsColor.orange, g);
	}
	
	private void drawExplosion(double drawX, double drawY, IGraphics g) {
		final double ratio = (System.currentTimeMillis() - explodeStarted) / explodedTime;		
		final double radius = ratio * splashRadius;
		drawCircle(drawX, drawY, radius, TsColor.orange, g);
	}
	
	private void drawCircle(double x, double y, double radius, TsColor color, IGraphics g) {
		g.setColor(color);
		float leftX = (float) (x - radius);
		float topY = (float) (y - radius);
		float diam = (float) (radius * 2);
		g.drawOval(leftX, topY, diam, diam);
	}

}
