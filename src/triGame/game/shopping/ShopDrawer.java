package triGame.game.shopping;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.particles.Particle;
import tSquare.util.Observer;
import triGame.game.entities.PointParticle;

public class ShopDrawer {
	private static final int particlesPerPoints = 4;
	private static final int drawX = 10, drawY = 5;
	
	private final ArrayList<Particle> particles = new ArrayList<Particle>(50);
	
	
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
			for (int i = 0; i < mult; i++) {
				double angle = Math.random() * Math.PI * 2;
				particles.add(new PointParticle.Flying(drawX, drawY, angle, 50));
			}
		}
	}
	
	public void draw(int frameDelta, Graphics2D g, ViewRect rect) {
		for (Iterator<Particle> it = particles.iterator(); it.hasNext(); ) {
			Particle p = it.next();
			if (p.isExpired())
				it.remove();
			else
				p.draw(frameDelta, g, rect);
		}
	}
}