package net.xuset.tSquare.paths;

import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.rect.IRectangleR;

/*
 * TODO fix int round down on entities whose width or height are less than one
 * examples: zombies, players, projectiles.
 */

public class ObjectGrid{
	public final int gridWidth;
	public final int gridHeight;
	//public final int blockWidth;
	//public final int blockHeight;
	
	private int modCount = 0;
	private boolean[][] grid;
	
	public int getModCount() { return modCount; }

//	public ObjectGrid(int areaWidth, int areaHeight) {//, int blockWidth, int blockHeight) {
//		gridWidth = (areaWidth / blockWidth) +
//				((areaWidth % blockWidth == 0) ? 0 : 1);
//		gridHeight = (areaHeight / blockHeight) +
//				((areaHeight % blockHeight == 0) ? 0 : 1);
//		//this.blockWidth = blockWidth;
//		//this.blockHeight = blockHeight;
//		grid = new boolean[gridWidth][gridHeight];
//	}
	
	public ObjectGrid(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		grid = new boolean[gridWidth][gridHeight];
	}
	
	public boolean turnOnBlock(IPointR p) {
		return turnOnBlock(p.getX(), p.getY());
	}
	public boolean turnOnBlock(double x, double y) {
		return setBlockValue((int) x, (int) y, true);
	}
	public boolean turnOffBlock(IPointR p) {
		return turnOffBlock(p.getX(), p.getY());
	}
	public boolean turnOffBlock(double x, double y) {
		return setBlockValue((int) x, (int) y, false);
	}
	private boolean setBlockValue(int x, int y, boolean value) {
		//int newX = (int) (x/this.blockWidth);
		//int newY = (int) (y/this.blockHeight);
		int newX = x;
		int newY = y;
		return setGridValue(newX, newY, value);
	}
	
	public boolean setGridValue(int x, int y, boolean value) {
		if (x >= gridWidth || x < 0 || y >= gridHeight || y < 0)
			return false;
		grid[x][y] = value;
		modCount++;
		return true;
	}
	
	public boolean turnOnRectange(IRectangleR r) {
		return setRectangleValue((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(), true);
	}
	public boolean turnOnRectangle(double x, double y, double width, double height) {
		return setRectangleValue((int) x, (int) y, (int) width, (int) height, true);
	}
	public boolean turnOnRectangle(int x, int y, int width, int height) {
		return setRectangleValue(x, y, width, height, true);
	}
	public boolean turnOffRectange(IRectangleR r) {
		return setRectangleValue((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight(), false);
	}
	public boolean turnOffRectangle(double x, double y, double width, double height) {
		return setRectangleValue((int) x, (int) y, (int) width, (int) height, false);
	}
	public boolean turnOffRectangle(int x, int y, int width, int height) {
		return setRectangleValue(x, y, width, height, false);
	}
	private boolean setRectangleValue(int x, int y, int width, int height, boolean value) {
		int topLeftX = x;// / blockWidth;
		int topLeftY = y;// / blockHeight;
		int bottomRightX = (x + width);// / blockWidth;
		int bottomRightY = (y + height);// / blockHeight;
		boolean allInBounds = true;
		for (int newX = topLeftX; newX < bottomRightX; newX++) {
			for (int newY = topLeftY; newY < bottomRightY; newY++) {
				if (setGridValue(newX, newY, value) == false)
					allInBounds = false;
			}
		}
		modCount++;
		return allInBounds;
	}
	
	public boolean isRectangleOpen(IRectangleR r) {
		return isRectangleOpen((int) r.getX(), (int) r.getY(), (int) r.getWidth(), (int) r.getHeight());
	}
	public boolean isRectangleOpen(double x, double y, double w, double h) {
		return isRectangleOpen((int) x, (int) y, (int) w, (int) h);
	}
	
	public boolean isRectangleOpen(int x, int y, int width, int height) {
		width = width == 0 ? 1 : width;
		height = height == 0 ? 1 : height;
		int topLeftX = x;// / blockWidth;
		int topLeftY = y;// / blockHeight;
		int bottomRightX = (x + width);// / blockWidth;
		int bottomRightY = (y + height);// / blockHeight;
		boolean allOpen = true;
		for (int newX = topLeftX; newX < bottomRightX; newX++) {
			for (int newY = topLeftY; newY < bottomRightY; newY++) {
				//allOpen = isOpen(newX, newY) && allOpen && true;
				if (isOpen(newX, newY) == false)
					allOpen = false;
			}
		}
		return allOpen;
	}
	
	public boolean isBlockOpen(int x, int y) {
		int newX = x;///blockWidth;
		int newY = y;///blockHeight;
		if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
			return (grid[newX][newY] == false);
		}
		return false;
	}
	public boolean isBlockOpen(double x, double y) {
		return isBlockOpen((int) x, (int) y);
	}
	public boolean isBlockOpen(IPointR p) {
		return isBlockOpen((int) p.getX(), (int) p.getY());
	}
	
	public boolean isOpen(int x, int y) { 
		if (isInBounds(x, y) && grid[x][y] == false)
			return true;
		return false;
	}
	
	private boolean isInBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < gridWidth && y < gridHeight;
	}
	
//	public int roundToGridX(double x) {
//		return ((int) x / blockWidth) * blockWidth;
//	}
//	public int roundToGridY(double y) {
//		return ((int) y / blockHeight) * blockHeight;
//	}
//	public Point roundToGrid(double x, double y) {
//		return new Point(roundToGridX(x), roundToGridY(y));
//	}
//	public Point roundToGrid(Point p) {
//		p.x = roundToGridX(p.x);
//		p.y = roundToGridY(p.y);
//		return p;
//	}
}
