package net.xuset.triGame.game.entities;

import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetVar;
import net.xuset.objectIO.netObj.NetVarListener;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.particles.ParticleController;


public class PointWell extends Entity {
	public static final String SPRITE_ID = "media/PointWell.png";
	private static final int maxParticles = 5;
	private static final int initialPoints = 2750;
	
	private final PointParticle.Hovering[] particles = new PointParticle.Hovering[maxParticles];
	private NetVar.nInt particleCount;
	private int startingPoints;
	private int pointsLeft;
	
	public boolean takePoints(int amount) {
		if (isEmpty())
			return false;
		
		pointsLeft -= amount;
		int count = getParticleCount();
		if (particleCount.get() != count) {
			particleCount.set(count);
			updateParticleEntities();
		}
		return !isEmpty();
	}
	
	public boolean isEmpty() { return pointsLeft <= 0; }

	protected PointWell(double startX, double startY, ParticleController pc, EntityKey key) {
		super(SPRITE_ID, startX, startY, key);
		
		startingPoints = pointsLeft = initialPoints;
		for (int i = 0; i < particles.length; i++) {
			particles[i] = new PointParticle.Hovering(getCenterX(), getCenterY());
			pc.addParticle(particles[i]);
		}
	}
	
	@Override
	protected void setNetObjects(NetClass objClass) {
		particleCount = new NetVar.nInt("pcount", maxParticles);
		objClass.addObj(particleCount);
		particleCount.setListener(new NetVarListener<Integer>() {
			@Override
			public void onVarChange(Integer newValue) {
				updateParticleEntities();
			}
		});
	}
	
	private void updateParticleEntities() {
		System.out.println("new count = " + particleCount.get());
		int count = particleCount.get();
		if (particles == null)
			return;
		
		for (int i = particles.length - 1; i >= count && i >= 0; i--) {
			if (particles[i] == null)
				continue;
			particles[i].setExpired();
			particles[i] = null;
		}
		
		int est = estimateMaxPoints(count + 1);
		if (pointsLeft > est)
			pointsLeft = est;
		if (count <= 0)
			pointsLeft = 0;
		
	}
	
	private int estimateMaxPoints(int particleCount) {
		return particleCount * (startingPoints / particles.length);
	}
	
	private int getParticleCount() {
		return pointsLeft / (startingPoints / particles.length);
	}

}
