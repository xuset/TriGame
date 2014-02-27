package net.xuset.triGame.game.shopping;

import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.game.particles.Particle;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.PointParticle;



public class ShopDrawer {
	private static final int particlesPerPoints = 4;
	private static final int particleSpawnDelay = 10;
	private static final double drawX = 1 / 5.0, drawY = 1 / 10.0;
	
	private final ArrayList<Particle> particles = new ArrayList<Particle>(50);
	
	private int particlesToSpawn = 0;
	private long nextParticleSpawn = 0;
	
	
	public ShopDrawer(Observer<ShopManager> shop) {
		shop.watch(new ShopObserver());
	}
	
	int lastPoints = 300;
	private class ShopObserver implements Observer.Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			int diff = t.getPointCount() - lastPoints;
			lastPoints = t.getPointCount();
			int mult = diff / particlesPerPoints;
			if (mult > 0)
				particlesToSpawn += mult;
		}
	}
	
	public void draw(int frameDelta, IGraphics g) {
		if (particlesToSpawn > 50)
			nextParticleSpawn = 0;
		
		if (particlesToSpawn > 0 && nextParticleSpawn < System.currentTimeMillis()) {

			double angle = Math.random() * Math.PI * 2;
			particles.add(new PointParticle.Flying(drawX, drawY, angle, 50));
			particlesToSpawn--;
			nextParticleSpawn = System.currentTimeMillis() + particleSpawnDelay;
		}
		
		for (Iterator<Particle> it = particles.iterator(); it.hasNext(); ) {
			Particle p = it.next();
			if (p.isExpired())
				it.remove();
			else
				p.draw(frameDelta, g);
		}
	}
}
