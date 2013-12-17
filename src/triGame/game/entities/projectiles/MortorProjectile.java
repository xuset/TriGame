package triGame.game.entities.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import triGame.game.ManagerService;
import triGame.game.entities.zombies.Zombie;

public class MortorProjectile extends Projectile {
	public static final String SPRITE_ID = "media/MortorProjectile.png";
	private static final long explodedTime = 700;
	
	private boolean collided = false;
	private long explodeStarted = 0;
	
	public int splashRadius = 100;
	
	protected MortorProjectile(String sSpriteId, double startX, double startY,
			double angle, int speed, int damage,
			ManagerService managers, EntityKey key) {
		
		super(sSpriteId, startX, startY, angle, speed, damage, true, managers, key);
		
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (!collided) {
			super.draw(g, rect);
			return;
		}
		
		double ratio = (System.currentTimeMillis() - explodeStarted + 0.0) / explodedTime;
		if (ratio > 1)
			remove();
		
		int radius = (int) (ratio * splashRadius);
		g.setColor(Color.orange);
		g.drawOval((int) (getX() - radius), (int) (getY() - radius), radius * 2, radius * 2);
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
				double dx = z.getX() - getX();
				double dy = z.getY() - getY();
				
				if (dx * dx + dy * dy < splashRadius * splashRadius) {
					z.hitByProjectile(damage);
				}
			}
		}
		
		if (collided)
			explodeStarted = System.currentTimeMillis();
	}

}
