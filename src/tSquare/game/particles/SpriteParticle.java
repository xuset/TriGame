package tSquare.game.particles;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.imaging.Sprite;

public abstract class SpriteParticle extends Particle {
	private Sprite sprite;
	
	protected boolean draw = true;
	protected int x = 0;
	protected int y = 0;
	
	protected abstract void setXY(int delta);
	
	public SpriteParticle(String spriteId) {
		sprite = Sprite.add(spriteId);
	}

	@Override
	public void draw(int delta, Graphics2D g, ViewRect rect) {
		setXY(delta);
		if (draw) {
			sprite.draw(x, y, g);
		}
	}

	@Override
	public boolean isExpired() {
		return false;
	}

}
