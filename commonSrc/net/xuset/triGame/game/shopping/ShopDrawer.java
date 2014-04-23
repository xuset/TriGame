package net.xuset.triGame.game.shopping;

import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.game.particles.Particle;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.PointParticle;



public class ShopDrawer {
	private static final int particlesPerSecond = 100;
	private static final int particlesPerPoints = 1;
	private static final double drawX = 2/5.0f, drawY = 2/5.0f, drawR = 2.0;
	
	private final ArrayList<Particle> particles = new ArrayList<Particle>(50);
	
	private long nextParticleSpawn = System.currentTimeMillis();
	
	private int particleGains = 0, particleLoss = 0, lastPointCount = 0;
	
	
	public ShopDrawer(Observer<ShopManager> shop) {
		shop.watch(new ShopObserver());
	}
	private class ShopObserver implements Observer.Change<ShopManager> {
		@Override
		public void observeChange(ShopManager t) {
			int diff = t.getPointCount() - lastPointCount;
			lastPointCount = t.getPointCount();
			
			int mult = diff / particlesPerPoints;
			if (mult > 0)
				particleGains += mult;
			else
				particleLoss = particleLoss - mult;
		}
	}
	
	public void draw(int frameDelta, IGraphics g) {
		while (nextParticleSpawn < System.currentTimeMillis())
			createNew();
		
		for (Iterator<Particle> it = particles.iterator(); it.hasNext(); ) {
			Particle p = it.next();
			if (p.isExpired())
				it.remove();
			else
				p.draw(frameDelta, g);
		}
	}
	
	private void createNew() {
			if (particleGains > 0)
				createIncoming();
			if (particleLoss > 0)
				createOutgoing();
			nextParticleSpawn += 1000/particlesPerSecond;
	}
	
	private void createOutgoing() {
		double angle = Math.random() * Math.PI * 2;
		particles.add(new PointParticle.Flying(drawX, drawY, angle, drawR));
		particleLoss--;
	}
	
	private void createIncoming() {
		double angle = Math.random() * Math.PI * 2;
		double x = drawX + drawR * Math.cos(angle);
		double y = drawY + drawR * Math.sin(angle);
		angle = Math.PI + angle;
		particles.add(new PointParticle.Flying(x, y, angle, drawR));
		particleGains--;
	}
}
