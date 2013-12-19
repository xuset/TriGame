package triGame.game.entities.projectiles;

import java.awt.Color;
import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.EntityKey;
import triGame.game.ManagerService;
import triGame.game.entities.zombies.Zombie;

public class MortarProjectile extends Projectile {
	public static final String SPRITE_ID = "media/MortarProjectile.png";
	private static final long explodedTime = 300;
	
	private boolean collided = false;
	private long explodeStarted = 0;
	
	public int splashRadius = 50;
	
	protected MortarProjectile(String sSpriteId, double startX, double startY,
			double angle, int speed, int damage,
			ManagerService managers, EntityKey key) {
		
		super(sSpriteId, startX, startY, angle, speed, damage, true, managers, key);
		
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (collided) {
			double ratio = (System.currentTimeMillis() - explodeStarted + 0.0) / explodedTime;
			if (ratio > 1)
				remove();
			
			int radius = (int) (ratio * splashRadius);
			g.setColor(Color.orange);
			g.drawOval((int) (getCenterX() - radius - rect.getX()),
					(int) (getCenterY() - radius - rect.getY()),
					radius * 2, radius * 2);
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
				double dx = z.getCenterX() - getCenterX();
				double dy = z.getCenterY() - getCenterY();
				
				if (dx * dx + dy * dy < splashRadius * splashRadius) {
					z.hitByProjectile(damage);
				}
			}
		}
		
		if (collided)
			explodeStarted = System.currentTimeMillis();
	}

}
