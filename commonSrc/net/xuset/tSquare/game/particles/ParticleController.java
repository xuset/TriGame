package net.xuset.tSquare.game.particles;

import java.util.ArrayList;
import java.util.Iterator;

import net.xuset.tSquare.game.Game;
import net.xuset.tSquare.game.GameDrawable;
import net.xuset.tSquare.imaging.IGraphics;



public class ParticleController implements GameDrawable{
	private final Game game;
	public final ArrayList<Particle> particles = new ArrayList<Particle>();
	
	public ParticleController(Game game) {
		this.game = game;
	}

	public void addParticle(Particle p) {
		particles.add(p);
	}
	
	public void removeParticle(Particle p) {
		particles.remove(p);
	}

	@Override
	public void draw(IGraphics g) {
		
		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle p = it.next();
			p.draw(game.getDelta(), g);
			if (p.isExpired()) {
				it.remove();
			}
		}
	}

	//@Override
	//public void doAction() {
	//	draw();
	//}

	//@Override
	//public boolean isExpired() { return false; }
	
}
