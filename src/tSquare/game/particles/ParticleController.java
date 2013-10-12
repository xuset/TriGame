package tSquare.game.particles;

import java.util.ArrayList;

import tSquare.events.Event;
import tSquare.game.Game;
import tSquare.game.GameIntegratable;

public class ParticleController implements GameIntegratable, Event{
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
	public void performLogic() { }

	@Override
	public void draw() {
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			p.draw(game.getDelta());
			if (p.isExpired()) {
				particles.remove(i);
				i--;
			}
		}
	}

	@Override
	public void doAction() {
		draw();
	}

	@Override
	public boolean isExpired() { return false; }
	
}
