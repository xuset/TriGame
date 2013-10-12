package tSquare.game.particles;

public abstract class Particle {
	public abstract void draw(int delta);
	public abstract boolean isExpired();
}
