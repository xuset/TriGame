package net.xuset.triGame.game.entities;

import net.xuset.tSquare.game.particles.SpriteParticle;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;

public abstract class PointParticle extends SpriteParticle{
	public static final String SPRITE_ID = "pointParticle";
	
	protected final double startX, startY;
	protected final Sprite sprite = Sprite.get(SPRITE_ID);
	
	
	public PointParticle(double x, double y) {
		super(SPRITE_ID);
		startX = x;
		startY = y;
	}

	@Override
	public void draw(int frameDelta, IGraphics g) {
		setXY(frameDelta);
		//if (draw && g.getView().isInside(x, y, sprite.getWidth(), sprite.getHeight()))
			sprite.draw((float) x, (float) y, g);
		
	}
	
	public static class Flying extends PointParticle {
		/*
		 * This class is set up to NOT use a scaled or transformed graphics instance for
		 * drawing. using the normal scaled and transformed graphics instance for drawing
		 * will produce unexpected results.
		 */
		private static final int speed = 75;
		private final double angle, distance;
		private final long startTime;

		public Flying(double x, double y, double angle, double distance) {
			super(x, y);
			this.angle = angle;
			this.distance = distance;
			startTime = System.currentTimeMillis();
		}

		@Override
		protected void setXY(int delta) {
			long time = System.currentTimeMillis() - startTime;
			double currentDist = speed * time / 1000.0;
			x = (startX + Math.cos(angle) * currentDist);
			y = (startY + Math.sin(angle) * currentDist);
		}
		
		@Override
		public boolean isExpired() {
			double dx = startX - x;
			double dy = startY - y;
			return (dx * dx + dy * dy > distance * distance);
		}
		
		@Override
		public void draw(int frameDelta, IGraphics g) {
			setXY(frameDelta);
			sprite.draw((float) x, (float) y, g);
		}
		
	}
	
	public static class Floating extends PointParticle {
		public double height = 35 / 50.0, width = 8 / 50.0;

		protected int progress = 0, timeDuration = 800;
		
		public Floating(double x, double y, int timeDuration) {
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
			x = (startX + width * Math.sin(ratio * 4 * Math.PI));
			y = (startY - ratio * height);
			
			if (isExpired())
				draw = false;
		}
		
	}
	
	public static class Hovering extends PointParticle {
		private boolean expired = false;
		private double boxRadius = 35 / 50.0;
		private int jumpDelta = (int) (Math.random() * 300) + 200;
		private long lastJumpTime = 0l;
		
		public Hovering(double x, double y) {
			super(x, y);
		}

		@Override public boolean isExpired() { return expired; }

		@Override
		protected void setXY(int delta) {
			if (lastJumpTime + jumpDelta < System.currentTimeMillis()) {
				x = (startX + Math.random() * boxRadius - boxRadius / 2);
				y = (startY + Math.random() * boxRadius - boxRadius / 2);
				lastJumpTime = System.currentTimeMillis();
			}
			
		}
		
		public void setExpired() {
			expired = true;
		}
		
	}
}