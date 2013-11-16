package triGame.game.entities;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;

public class PointWell extends Entity {
	public static final String SPRITE_ID = "media/PointWell.png";
	
	private final PointParticle[] particles = new PointParticle[5];

	protected PointWell(double startX, double startY, EntityKey key, ParticleController pc) {
		super(SPRITE_ID, startX, startY, key);
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new PointParticle.Hovering((int) getCenterX(), (int) getCenterY());
			pc.addParticle(particles[i]);
		}
	}

}
