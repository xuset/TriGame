package tSquare.game.particles;

import java.awt.Graphics;

import tSquare.imaging.Sprite;

public abstract class SpriteParticle extends Particle {
	private Sprite sprite;
	private Graphics graphics;
	
	protected boolean draw = true;
	protected int x = 0;
	protected int y = 0;
	
	protected abstract void setXY(int delta);
	
	public SpriteParticle(String spriteId, Graphics g) {
		sprite = Sprite.add(spriteId);
		graphics = g;
	}

	@Override
	public void draw(int delta) {
		setXY(delta);
		if (draw) {
			sprite.draw(x, y, graphics);
		}
	}

	@Override
	public boolean isExpired() {
		return false;
	}

}
