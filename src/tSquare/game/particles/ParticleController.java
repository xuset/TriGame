package tSquare.game.particles;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import tSquare.game.Game;
import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;

public class ParticleController implements GameIntegratable{
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
	public void performLogic(int frameDelta) { }

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		
		for (Iterator<Particle> it = particles.iterator(); it.hasNext();) {
			Particle p = it.next();
			p.draw(game.getDelta(), g, rect);
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
