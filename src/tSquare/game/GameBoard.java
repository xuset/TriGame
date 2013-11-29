package tSquare.game;


import java.awt.Point;

import tSquare.math.Rectangle;


public class GameBoard{
	private int width;
	private int height;
	public ViewRect viewable;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public GameBoard(int width, int height) {
		this.width = width;
		this.height = height;
		viewable = new ViewRect(width, height);
	}
	public GameBoard(int width, int height, DrawBoard drawBoard) {
		this.width = width;
		this.height = height;
		viewable = new DrwBoardViewRect(drawBoard);
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
	
	public void translateToScreen(Point p) {
		p.setLocation(p.getX() - viewable.getX(), p.getY() - viewable.getY());
	}
	
	public boolean isInsideBoard(double x, double y) {
		if ((x < 0 || x > width) || (y < 0 || y > height)) {
			return false;
		}
		return true;
	}
	
	public boolean isInsideViewable(int x, int y, int width, int height) {
		if (x + width > viewable.getX() && y + height > viewable.getY() && x < viewable.getX() + viewable.getWidth() && y < viewable.getY() + viewable.getHeight())	
			return true;
		return false;
	}
	
	public boolean isInsideViewable(int x, int y) {
		if (x > viewable.getX() && y > viewable.getY() && x < viewable.getX() + viewable.getWidth() && y < viewable.getY() + viewable.getHeight())
			return true;
		return false;
	}
	
	public class ViewRect extends Rectangle {
		private static final long serialVersionUID = -8685354307404082973L;

		public ViewRect(int width, int height) {
			super(0, 0, width, height);
		}
		
		public void translateToView(Point p) { p.x = (int) (p.x - getX()); p.y = (int) (p.y - getY()); }
		
		public boolean isInside(double x, double y) {
			return (x > getX() && y > getY() && x < getX() + getWidth() &&
					y < getY() + getHeight());
		}
		
		public boolean isInside(double x, double y, double width, double height) {
			return (x + width > getX() && x < getX() + getWidth() &&
					y + height > getY() && y < getY() + getHeight());
		}
	}
	
	public class DrwBoardViewRect extends ViewRect {
		private static final long serialVersionUID = 7415575589993188408L;
		private DrawBoard drawBoard;

		public DrwBoardViewRect(DrawBoard drawBoard) {
			super(drawBoard.getWidth(), drawBoard.getHeight());
			this.drawBoard = drawBoard;
		}
		
		public double getWidth() { return drawBoard.getWidth(); }
		public double getHeight() { return drawBoard.getHeight(); }
		
	}
}
