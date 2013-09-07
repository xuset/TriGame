package triGame.game.projectile;

import tSquare.game.entity.Entity;
import triGame.game.entities.zombies.Zombie;

public class CollisionHandling {
	static boolean doEdgeCollision(Entity e, Projectile p, ProjectileContainer container) {
		if (container.gameBoard.isInsideBoard(e.getX(), e.getY()) == false) {
			p.remove();
			return true;
		}
		return false;
	}
	
	static boolean doZombieCollision(Entity e, int damage, Projectile p, ProjectileContainer container) {
		Zombie z = p.checkZombieCollision(e);
		if (z != null) {
			p.remove();
			if (container.getUserId() == p.ownerId) {
				z.modifyHealth(damage);
				z.hitBack(10);
				container.getShop().points += 1;
			}
			return true;
		}
		return false;
	}
	
	static boolean doBuildingCollision(Entity e, int damage, Projectile p, ProjectileContainer container) {
		Entity b = p.checkBuildingCollision(e);
		if (b != null) {
			p.remove();
			if (container.getUserId() == p.ownerId)
				b.modifyHealth(damage);
			return true;
		}
		return false;
	}
}
