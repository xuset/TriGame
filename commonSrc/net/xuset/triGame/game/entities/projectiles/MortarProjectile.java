package net.xuset.triGame.game.entities.projectiles;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.zombies.Zombie;


public class MortarProjectile extends Projectile {
	public static final String SPRITE_ID = "media/MortarProjectile.png";
	private static final double explodedTime = 300;
	private static final int splashRadius = 50;
	private static final double blowBackTime = 100;
	private static final int blowBackRadius = 15;
	
	private boolean collided = false;
	private long explodeStarted = 0;
	private long timeCreated = 0;
	
	protected MortarProjectile(double startX, double startY, double angle,
			double speed, int damage, ManagerService managers, GameGrid gameGrid) {
		
		super(SPRITE_ID, startX, startY, angle, speed, damage, true, managers, gameGrid);
		timeCreated = System.currentTimeMillis();
	}
	
	protected MortarProjectile(GameGrid gameGrid, EntityKey key) {
		super(gameGrid, key);
		timeCreated = System.currentTimeMillis();
	}
	
	@Override
	public void draw(IGraphics g) {
		IRectangleR rect = g.getView();
		final int drawX = (int) (getCenterX() - rect.getX());
		final int drawY = (int) (getCenterY() - rect.getY());
		
		drawBlowBack(drawX, drawY, g);
		
		if (collided) {
			drawExplosion(drawX, drawY, g);
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
		
		Zombie zombie = collidedWithFirst(managers.zombie.list);
		if (zombie != null) {
			collided = true;

			for (Zombie z : managers.zombie.list) {
				double distance = PointR.distance(getCenterX(), getCenterY(), z.getCenterX(), z.getCenterY());
				double ratio = 1 - distance / splashRadius;
				if (ratio > 0)
					z.hitByProjectile((int) (ratio * damage.get()));
			}
			
		}
		
		if (collided)
			explodeStarted = System.currentTimeMillis();
	}
	
	private void drawBlowBack(int drawX, int drawY, IGraphics g) {
		final double blowBackRatio = (System.currentTimeMillis() - timeCreated) / blowBackTime;
		final int radius = (int) (blowBackRatio * blowBackRadius);
		if (blowBackRatio < 1 && blowBackRatio > 0)
			drawCircle(drawX, drawY, radius, TsColor.orange, g);
	}
	
	private void drawExplosion(int drawX, int drawY, IGraphics g) {
		final double ratio = (System.currentTimeMillis() - explodeStarted) / explodedTime;		
		final int radius = (int) (ratio * splashRadius);
		drawCircle(drawX, drawY, radius, TsColor.orange, g);
	}
	
	private void drawCircle(int x, int y, int radius, TsColor color, IGraphics g) {
		g.setColor(color);
		g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}

}
