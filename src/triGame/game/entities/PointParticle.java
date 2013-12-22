package triGame.game.entities;

import java.awt.Graphics2D;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.particles.SpriteParticle;
import tSquare.imaging.Sprite;

public abstract class PointParticle extends SpriteParticle{
	public static final String SPRITE_ID = "pointParticle";
	
	protected final int startX, startY;
	protected final Sprite sprite = Sprite.get(SPRITE_ID);
	
	
	public PointParticle(int x, int y) {
		super(SPRITE_ID);
		startX = x;
		startY = y;
	}

	@Override
	public void draw(int frameDelta, Graphics2D g, ViewRect rect) {
		setXY(frameDelta);
		int screenX = (int) (x - rect.getX());
		int screenY = (int) (y - rect.getY());
		if (draw && rect.isInside(x, y, sprite.getWidth(), sprite.getHeight()))
			sprite.draw(screenX, screenY, g);
		
	}
	
	public static class Flying extends PointParticle {
		private static final int speed = 100;
		private final double angle, distance;
		private final long startTime;

		public Flying(int x, int y, double angle, double distance) {
			super(x, y);
			this.angle = angle;
			this.distance = distance;
			startTime = System.currentTimeMillis();
		}

		@Override
		protected void setXY(int delta) {
			long time = System.currentTimeMillis() - startTime;
			double currentDist = speed * time / 1000.0;
			x = (int) (startX + Math.cos(angle) * currentDist);
			y = (int) (startY + Math.sin(angle) * currentDist);
		}
		
		@Override
		public boolean isExpired() {
			int dx = startX - x;
			int dy = startY - y;
			return (dx * dx + dy * dy > distance * distance);
		}
		
		@Override
		public void draw(int frameDelta, Graphics2D g, ViewRect rect) {
			setXY(frameDelta);
			sprite.draw(x, y, g);
		}
		
	}
	
	public static class Floating extends PointParticle {
		public int height = 35, width = 8;

		protected int progress = 0, timeDuration = 800;
		
		public Floating(int x, int y, int timeDuration) {
			super(x, y);
			this.timeDuration = timeDuration;
		}
		
		public void reset() {
			progress = 0;
			draw = true;
		}

		@Override public boolean isExpired() { return progress > timeDuration; }

		@Override
		protected void setXY(int delta) {
			progress += delta;
			double ratio = ((double) progress) / timeDuration;
			x = (int) (startX + width * Math.sin(ratio * 4 * Math.PI));
			y = (int) (startY - ratio * height);
			
			if (isExpired())
				draw = false;
		}
		
	}
	
	public static class Hovering extends PointParticle {
		private boolean expired = false;
		private int boxRadius = 35;
		private int jumpDelta = (int) (Math.random() * 300) + 200;
		private long lastJumpTime = 0l;
		
		public Hovering(int x, int y) {
			super(x, y);
		}

		@Override public boolean isExpired() { return expired; }

		@Override
		protected void setXY(int delta) {
			if (lastJumpTime + jumpDelta < System.currentTimeMillis()) {
				x = (int) (startX + Math.random() * boxRadius - boxRadius / 2);
				y = (int) (startY + Math.random() * boxRadius - boxRadius / 2);
				lastJumpTime = System.currentTimeMillis();
			}
			
		}
		
		public void setExpired() {
			expired = true;
		}
		
	}
}