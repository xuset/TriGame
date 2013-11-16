package tSquare.game.particles;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;

public abstract class Particle {
	public abstract void draw(int delta, Graphics2D g, ViewRect rect);
	public abstract boolean isExpired();
}
