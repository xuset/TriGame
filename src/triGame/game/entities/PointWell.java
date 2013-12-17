package triGame.game.entities;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;

public class PointWell extends Entity {
	public static final String SPRITE_ID = "media/PointWell.png";
	
	private final PointParticle.Hovering[] particles = new PointParticle.Hovering[5];
	private final NetVar.nInt particleCount;
	private int startingPoints;
	private int pointsLeft;
	
	public boolean takePoints(int amount) {
		if (isEmpty())
			return false;
		
		pointsLeft -= amount;
		int index = pointsLeft / (startingPoints / particles.length);
		if (particleCount.get() != index) {
			particleCount.set(index);
			objClass.update();
			particleCount.event.onChange(particleCount, null);
		}
		return !isEmpty();
	}
	
	public boolean isEmpty() { return pointsLeft <= 0; }

	protected PointWell(double startX, double startY, EntityKey key, ParticleController pc) {
		super(SPRITE_ID, startX, startY, key);
		
		particleCount = new NetVar.nInt(particles.length, "pcount", objClass);
		particleCount.event = new NetVar.OnChange<Integer>() {

			@Override
			public void onChange(NetVar<Integer> var, Connection c) {
				int index = var.get();
				if (index < 0 ||particles[index] == null)
					return;
				
				particles[index].setExpired();
				particles[index] = null;
			}
			
		};
		
		startingPoints = pointsLeft = (int) (Math.random() * 500 + 2500);
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new PointParticle.Hovering((int) getCenterX(), (int) getCenterY());
			pc.addParticle(particles[i]);
		}
	}

}
