package net.xuset.tSquare.game.particles;

import net.xuset.tSquare.imaging.IGraphics;

public abstract class Particle {
	public abstract void draw(int delta, IGraphics g);
	public abstract boolean isExpired();
}
