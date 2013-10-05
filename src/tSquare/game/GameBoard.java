package tSquare.game;


import java.awt.Graphics;
import java.awt.Image;

import tSquare.math.Point;
import tSquare.math.Rectangle;


public class GameBoard{

	private DrawBoard drawBoard;
	private int width;
	private int height;
	public CustomRectangle viewable;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public Graphics getGraphics() { return drawBoard.getDrawing(); }
	public DrawBoard getDrawBoard() { return drawBoard; }
	
	public GameBoard(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public GameBoard(int width, int height, DrawBoard drawBoard) {
		this.width = width;
		this.height = height;
		this.drawBoard = drawBoard;
		viewable = new CustomRectangle();
	}
	
	public void setDrawBoard(DrawBoard drawBoard) {
		this.drawBoard = drawBoard;
		viewable = new CustomRectangle();
	}
	
	public void draw(Image image, Point p) {
		draw(image, p.intX(), p.intY());
	}
	public void draw(Image image, int x, int y) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int topLeftX = (int) (x - viewable.getX());
		int topLeftY = (int) (y - viewable.getY());
		draw(image, topLeftX, topLeftY, topLeftX + width, topLeftY + height, 0, 0, width, height);
	}
	public void draw(Image image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2) {
		if (isInsideViewable(dx1, dy1, dx2 - dx1, dy2 - dy1))
			drawBoard.getDrawing().drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}
	
	public void centerViewWindowCordinates(double centerX, double centerY) {
		double x = (int) centerX - viewable.getWidth()/2.0;
		double y = (int) centerY - viewable.getHeight()/2.0;
		if (centerX > getWidth() - viewable.getWidth()/2.0)
			x = getWidth() - viewable.getWidth();
		if (centerX < viewable.getWidth()/2.0)
			x = 0;
		if (centerY > getHeight() - viewable.getHeight()/2.0)
			y = getHeight() - viewable.getHeight();
		if (centerY < viewable.getHeight()/2.0)
			y = 0;
		if (viewable.getWidth() > getWidth())
			x = 0;
		if (viewable.getHeight() > getHeight())
			y = 0;
		viewable.set(x, y);
	}
	
	public boolean isInsideBoard(double x, double y) {
		if ((x < 0 || x > width) || (y < 0 || y > height)) {
			return false;
		}
		return true;
	}
	
	public boolean isInsideViewable(int x, int y, int width, int height) {
		//if (x + width > viewable.getX() && x < viewable.getX() + viewable.getWidth() && y + height > viewable.getY() && y < viewable.getY() + viewable.getHeight())
		if (x + width >= 0 && y + height >= 0 && x < viewable.getWidth() && y < viewable.getHeight())	
			return true;
		return false;
	}
	
	public class CustomRectangle extends Rectangle {
		public CustomRectangle() {
			super(0, 0, drawBoard.getWidth(), drawBoard.getHeight());
		}
		public double getWidth() { return drawBoard.getWidth(); }
		public double getHeight() { return drawBoard.getHeight(); }
	}
}
