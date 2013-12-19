package triGame.game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import tSquare.game.GameIntegratable;
import tSquare.game.GameBoard.ViewRect;
import tSquare.game.entity.Entity;
import tSquare.imaging.ImageProcess;


public class TiledBackground implements GameIntegratable{
	public static final boolean IS_VOLATILE_IMAGE = false;
	
	private int x;
	private int y;
	private Image backgroundImage;
	private BufferedImage tileImage;
	private boolean positioned = false;
	
	public Entity centerTo;

	/**
	 * 
	 * @param drawBoard just needed to add Component listener for window resize
	 * @param centerTo the player so the background can move with the player
	 */
	public TiledBackground(Component screen, Entity centerTo) {
		this.centerTo = centerTo;
		tileImage = createTileImage();
		resizeImage(screen.getWidth(), screen.getHeight());
		screen.addComponentListener(new ComponentListener() {
	        public void componentResized(ComponentEvent evt) {
	        	resizeImage(evt.getComponent().getWidth(), evt.getComponent().getHeight());
	        }
			public void componentHidden(ComponentEvent arg0) { }
			public void componentMoved(ComponentEvent arg0) { }
			public void componentShown(ComponentEvent arg0) { }
    	});
	}
	
	private BufferedImage createTileImage() {
		final int gutterX = 5;
		final int gutterY = 5;
		tileImage = new BufferedImage(Params.BLOCK_SIZE, Params.BLOCK_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D tileImageg = (Graphics2D) tileImage.getGraphics();
		tileImageg.setColor(Color.DARK_GRAY);
		tileImageg.fillRect(0, 0, Params.BLOCK_SIZE, Params.BLOCK_SIZE);
		tileImageg.setColor(new Color(225, 225, 225));
		tileImageg.fillRect(gutterX, gutterY, Params.BLOCK_SIZE - 2 * gutterX, Params.BLOCK_SIZE - 2 * gutterY);
		tileImageg.dispose();
		return tileImage;
	}
	
	public void resizeImage(int drawBoardWidth, int drawBoardHeight) {		
		int width = ((drawBoardWidth % Params.BLOCK_SIZE == 0) ?
				drawBoardWidth / Params.BLOCK_SIZE :
				drawBoardWidth / Params.BLOCK_SIZE + 1) + 1;
		int height = ((drawBoardHeight % Params.BLOCK_SIZE == 0) ?
				drawBoardHeight / Params.BLOCK_SIZE :
				drawBoardHeight / Params.BLOCK_SIZE + 1) + 1; 
		
		if (IS_VOLATILE_IMAGE)
			backgroundImage = ImageProcess.createVolatileImage(
					width * Params.BLOCK_SIZE, height * Params.BLOCK_SIZE);
		else
			backgroundImage = ImageProcess.createCompatiableImage(
					width * Params.BLOCK_SIZE, height * Params.BLOCK_SIZE);
		
		Graphics2D backImageg = (Graphics2D) backgroundImage.getGraphics();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				backImageg.drawImage(tileImage, x * Params.BLOCK_SIZE, y * Params.BLOCK_SIZE, null);
			}
		}
		backImageg.dispose();
	}
	
	public void positionBackground(ViewRect rect) {
		double centerX = 0;
		double centerY = 0;
		if (centerTo != null) {
			centerX = centerTo.getCenterX();
			centerY = centerTo.getCenterY();
		}
		double pivotX = centerX - rect.getWidth()/2.0;
		double pivotY = centerY - rect.getHeight()/2.0;
		if (centerX > Params.GAME_WIDTH - rect.getWidth()/2.0)
			pivotX = Params.GAME_WIDTH - rect.getWidth();
		if (centerX < rect.getWidth()/2.0)
			pivotX = 0;
		if (centerY > Params.GAME_HEIGHT - rect.getHeight()/2.0)
			pivotY = Params.GAME_HEIGHT - rect.getHeight();
		if (centerY < rect.getHeight()/2.0)
			pivotY = 0;
		if (rect.getWidth() > Params.GAME_WIDTH)
			pivotX = 0;
		if (rect.getHeight() > Params.GAME_HEIGHT)
			pivotY = 0;
		x = (int) (-1 * (pivotX - ((int) (pivotX / Params.BLOCK_SIZE)) * Params.BLOCK_SIZE));
		y = (int) (-1 * (pivotY - ((int) (pivotY / Params.BLOCK_SIZE)) * Params.BLOCK_SIZE));
		positioned = true;
	}
	
	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		if (!positioned)
			positionBackground(rect);
		g.drawImage(backgroundImage, x, y, null);
		positioned = false;
	}
	
	@Override
	public void performLogic(int frameDelta) { }
}
