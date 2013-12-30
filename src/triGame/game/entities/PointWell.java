package triGame.game.entities;

import objectIO.connections.Connection;
import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;
import tSquare.game.particles.ParticleController;

public class PointWell extends Entity {
	public static final String SPRITE_ID = "media/PointWell.png";
	private static final int maxParticles = 5;
	
	private final PointParticle.Hovering[] particles = new PointParticle.Hovering[maxParticles];
	private NetVar.nInt particleCount;
	private int startingPoints;
	private int pointsLeft;
	
	public boolean takePoints(int amount) {
		if (isEmpty())
			return false;
		
		pointsLeft -= amount;
		int index = pointsLeft / (startingPoints / particles.length);
		if (particleCount.get() != index) {
			particleCount.set(index);
			sendUpdates();
		}
		return !isEmpty();
	}
	
	public boolean isEmpty() { return pointsLeft <= 0; }

	protected PointWell(double startX, double startY, ParticleController pc, EntityKey key) {
		super(SPRITE_ID, startX, startY, key);
		
		startingPoints = pointsLeft = (int) (Math.random() * 500 + 2500);
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new PointParticle.Hovering((int) getCenterX(), (int) getCenterY());
			pc.addParticle(particles[i]);
		}
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		particleCount = new NetVar.nInt(maxParticles, "pcount", objClass);
		particleCount.setEvent(true, new NetVar.OnChange<Integer>() {
			@Override
			public void onChange(NetVar<Integer> var, Connection c) {
				int index = var.get();
				if (index < 0 || particles == null || particles[index] == null)
					return;
				
				particles[index].setExpired();
				particles[index] = null;
			}
		});
	}

}
