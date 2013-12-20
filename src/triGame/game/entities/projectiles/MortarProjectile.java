package triGame.game.entities.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.entities.zombies.Zombie;

public class MortarProjectile extends Projectile {
	public static final String SPRITE_ID = "media/MortarProjectile.png";
	private static final double explodedTime = 300;
	private static final int splashRadius = 50;
	private static final double blowBackTime = 100;
	private static final int blowBackRadius = 15;
	
	private boolean collided = false;
	private long explodeStarted = 0;
	private long timeCreated = 0;
	
	protected MortarProjectile(String sSpriteId, double startX, double startY,
			double angle, int speed, int damage,
			ManagerService managers, EntityKey key) {
		
		super(sSpriteId, startX, startY, angle, speed, damage, true, managers, key);
		timeCreated = System.currentTimeMillis();
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		final int drawX = (int) (getCenterX() - rect.getX());
		final int drawY = (int) (getCenterY() - rect.getY());
		
		drawBlowBack(drawX, drawY, g);
		
		if (collided) {
			drawExplosion(drawX, drawY, g);
			if (System.currentTimeMillis() > explodeStarted + explodedTime)
				remove();
		} else {
			super.draw(g, rect);
		}
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (collided)
			return;
		
		moveForward(speed * frameDelta / 1000.0);
		collided = checkBounds();
		
		Zombie zombie = collidedWithFirst(managers.zombie.list);
		if (zombie != null) {
			collided = true;

			for (Zombie z : managers.zombie.list) {
				double distance = Point.distance(getCenterX(), getCenterY(), z.getCenterX(), z.getCenterY());
				double ratio = 1 - distance / splashRadius;
				if (ratio > 0)
					z.hitByProjectile((int) (ratio * damage));
			}
			
		}
		
		if (collided)
			explodeStarted = System.currentTimeMillis();
	}
	
	private void drawBlowBack(int drawX, int drawY, Graphics2D g) {
		final double blowBackRatio = (System.currentTimeMillis() - timeCreated) / blowBackTime;
		final int radius = (int) (blowBackRatio * blowBackRadius);
		if (blowBackRatio < 1 && blowBackRatio > 0)
			drawCircle(drawX, drawY, radius, Color.orange, g);
	}
	
	private void drawExplosion(int drawX, int drawY, Graphics2D g) {
		final double ratio = (System.currentTimeMillis() - explodeStarted) / explodedTime;		
		final int radius = (int) (ratio * splashRadius);
		drawCircle(drawX, drawY, radius, Color.orange, g);
	}
	
	private void drawCircle(int x, int y, int radius, Color color, Graphics2D g) {
		g.setColor(color);
		g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
	}

}
