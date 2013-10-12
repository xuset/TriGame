package triGame.game.entities;

import tSquare.game.GameBoard;
import tSquare.game.particles.SpriteParticle;
import tSquare.imaging.Sprite;

public abstract class PointParticle extends SpriteParticle{
	public static final String SPRITE_ID = "pointParticle";
	
	protected final int startX, startY;
	protected final Sprite sprite = Sprite.get(SPRITE_ID);
	protected final GameBoard board;
	
	
	public PointParticle(int x, int y, GameBoard board) {
		super(SPRITE_ID, board.getGraphics());
		startX = x;
		startY = y;
		this.board = board;
	}

	@Override
	public void draw(int delta) {
		setXY(delta);
		int screenX = (int) (x - board.viewable.getX());
		int screenY = (int) (y - board.viewable.getY());
		if (draw && board.isInsideViewable(x, y, sprite.getWidth(), sprite.getHeight()))
			sprite.draw(screenX, screenY, board.getGraphics());
		
	}
	
	public static class Floating extends PointParticle {

		protected int progress = 0, finishTime = 800;
		public Floating(int x, int y, GameBoard g) {
			super(x, y, g);
		}
		
		public void reset() {
			progress = 0;
			draw = true;
		}

		@Override public boolean isExpired() { return progress > finishTime; }

		@Override
		protected void setXY(int delta) {
			progress += delta;
			double ratio = ((double) progress) / finishTime;
			x = (int) (startX + 8 * Math.sin(ratio * 4 * Math.PI));
			y = (int) (startY - ratio * 35);
			
			if (isExpired())
				draw = false;
		}
		
	}
	
	public static class Hovering extends PointParticle {

		private int boxRadius = 35;
		private int jumpDelta = (int) (Math.random() * 300) + 200;
		private long lastJumpTime = 0l;
		
		public Hovering(int x, int y, GameBoard g) {
			super(x, y, g);
		}

		@Override public boolean isExpired() { return false; }

		@Override
		protected void setXY(int delta) {
			if (lastJumpTime + jumpDelta < System.currentTimeMillis()) {
				x = (int) (startX + Math.random() * boxRadius - boxRadius / 2);
				y = (int) (startY + Math.random() * boxRadius - boxRadius / 2);
				lastJumpTime = System.currentTimeMillis();
			}
			
		}
		
	}
}