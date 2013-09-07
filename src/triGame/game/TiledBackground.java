package triGame.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import tSquare.game.DrawBoard;
import tSquare.game.GameBoard;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;
import tSquare.imaging.ImageProccess;


public class TiledBackground implements GameIntegratable{
	public static final boolean IS_VOLATILE_IMAGE = true;
	
	private DrawBoard drawBoard;
	private int x;
	private int y;
	private Image backgroundImage;
	private int deltaX;
	private int deltaY;
	private BufferedImage tileImage;
	private boolean positioned = false;
	
	public Entity centerTo;
	public GameBoard gameBoard;

	
	public TiledBackground(GameBoard gameBoard, DrawBoard drawBoard, Entity centerTo, int deltaX, int deltaY) {
		this.centerTo = centerTo;
		this.gameBoard = gameBoard;
		this.drawBoard = drawBoard;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		tileImage = createTileImage();
		createImage();
		drawBoard.addComponentListener(new ComponentListener() {
	        public void componentResized(ComponentEvent evt) { resizeImage(); }
			public void componentHidden(ComponentEvent arg0) { }
			public void componentMoved(ComponentEvent arg0) { }
			public void componentShown(ComponentEvent arg0) { }
    	});
	}
	
	private BufferedImage createTileImage() {
		final int gutterX = 5;
		final int gutterY = 5;
		tileImage = new BufferedImage(deltaX, deltaY, BufferedImage.TYPE_INT_RGB);
		Graphics2D tileImageg = (Graphics2D) tileImage.getGraphics();
		tileImageg.setColor(Color.DARK_GRAY);
		tileImageg.fillRect(0, 0, deltaX, deltaY);
		tileImageg.setColor(new Color(225, 225, 225));
		tileImageg.fillRect(gutterX, gutterY, deltaX - 2 * gutterX, deltaY - 2 * gutterY);
		tileImageg.dispose();
		return tileImage;
	}
	
	public void createImage() {
		resizeImage();
	}
	
	public void resizeImage() {
		final int drawBoardWidth = drawBoard.getWidth();
		final int drawBoardHeight = drawBoard.getHeight();
		
		int width = ((drawBoardWidth % deltaX == 0) ? drawBoardWidth / deltaX : drawBoardWidth / deltaX + 1) + 1; 
		int height = ((drawBoardHeight % deltaY == 0) ? drawBoardHeight / deltaY : drawBoardHeight / deltaY + 1) + 1; 
		
		if (IS_VOLATILE_IMAGE)
			backgroundImage = ImageProccess.createVolatileImage(width * deltaX, height * deltaY);
		else
			backgroundImage = ImageProccess.createCompatiableImage(width * deltaX, height * deltaY);
		
		Graphics2D backImageg = (Graphics2D) backgroundImage.getGraphics();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				backImageg.drawImage(tileImage, x * deltaX, y * deltaY, null);
			}
		}
		backImageg.dispose();
	}
	
	public void positionBackground() {
		double centerX = 0;
		double centerY = 0;
		if (centerTo != null) {
			centerX = centerTo.getCenterX();
			centerY = centerTo.getCenterY();
		}
		double pivotX = centerX - gameBoard.viewable.getWidth()/2.0;
		double pivotY = centerY - gameBoard.viewable.getHeight()/2.0;
		if (centerX > gameBoard.getWidth() - gameBoard.viewable.getWidth()/2.0)
			pivotX = gameBoard.getWidth() - gameBoard.viewable.getWidth();
		if (centerX < gameBoard.viewable.getWidth()/2.0)
			pivotX = 0;
		if (centerY > gameBoard.getHeight() - gameBoard.viewable.getHeight()/2.0)
			pivotY = gameBoard.getHeight() - gameBoard.viewable.getHeight();
		if (centerY < gameBoard.viewable.getHeight()/2.0)
			pivotY = 0;
		if (gameBoard.viewable.getWidth() > gameBoard.getWidth())
			pivotX = 0;
		if (gameBoard.viewable.getHeight() > gameBoard.getHeight())
			pivotY = 0;
		x = (int) (-1 * (pivotX -  ((int) (pivotX / deltaX)) * deltaX));
		y = (int) (-1 * (pivotY -  ((int) (pivotY / deltaY)) * deltaY));
		positioned = true;
	}
	
	public void performLogic() {
		positionBackground();
	}
	
	public void draw() {
		if (!positioned)
			positionBackground();
		gameBoard.getGraphics().drawImage(backgroundImage, x, y, null);
		positioned = false;
	}
	
}
