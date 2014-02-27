package net.xuset.tSquare.game.particles;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;

public abstract class SpriteParticle extends Particle {
	private Sprite sprite;
	
	protected boolean draw = true;
	protected double x = 0;
	protected double y = 0;
	
	protected abstract void setXY(int delta);
	
	public SpriteParticle(String spriteId) {
		sprite = Sprite.add(spriteId);
	}

	@Override
	public void draw(int delta, IGraphics g) {
		setXY(delta);
		if (draw) {
			sprite.draw((float) x, (float) y, g);
		}
	}

	@Override
	public boolean isExpired() {
		return false;
	}

}
