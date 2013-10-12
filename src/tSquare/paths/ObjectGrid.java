package tSquare.paths;

import tSquare.game.GameBoard;
import tSquare.math.Point;


public class ObjectGrid {
	public int gridWidth;
	public int gridHeight;
	public int blockWidth;
	public int blockHeight;
	public int modCount = 0;
	
	private boolean[][] grid;
	
	public int getGridWidth() { return gridWidth; }
	public int getGridHeight() { return gridHeight; }
	public int getBlockWidth() { return blockWidth; }
	public int getBlockHeight() { return blockHeight; }

	public ObjectGrid(int gridWidth, int gridHeight, int blockWidth, int blockHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		grid = new boolean[gridWidth][gridHeight];
	}
	
	public ObjectGrid(GameBoard gameBoard, int blockWidth, int blockHeight) {
		this(gameBoard.getWidth() / blockWidth, gameBoard.getHeight() / blockHeight, blockWidth, blockHeight);
	}
	
	public ObjectGrid getReplica() {
		return new ObjectGrid(this.gridWidth, this.gridHeight, this.blockWidth, this.blockHeight);
	}
	
	public boolean turnOnBlock(Point p) {
		return turnOnBlock(p.x, p.y);
	}
	public boolean turnOnBlock(double x, double y) {
		return setBlockValue((int) x, (int) y, true);
	}
	public boolean turnOffBlock(Point p) {
		return turnOffBlock(p.x, p.y);
	}
	public boolean turnOffBlock(double x, double y) {
		return setBlockValue((int) x, (int) y, false);
	}
	private boolean setBlockValue(int x, int y, boolean value) {
		int newX = (int) (x/this.blockWidth);
		int newY = (int) (y/this.blockHeight);
		return setGridValue(newX, newY, value);
	}
	
	public boolean setGridValue(int x, int y, boolean value) {
		if (x >= gridWidth || x < 0 || y >= gridHeight || y < 0)
			return false;
		grid[x][y] = value;
		modCount++;
		return true;
	}
	
	public boolean turnOnRectangle(double x, double y, double width, double height) {
		return setRectangleValue((int) x, (int) y, (int) width, (int) height, true);
	}
	public boolean turnOnRectangle(int x, int y, int width, int height) {
		return setRectangleValue(x, y, width, height, true);
	}
	public boolean turnOffRectangle(double x, double y, double width, double height) {
		return setRectangleValue((int) x, (int) y, (int) width, (int) height, false);
	}
	public boolean turnOffRectangle(int x, int y, int width, int height) {
		return setRectangleValue(x, y, width, height, false);
	}
	private boolean setRectangleValue(int x, int y, int width, int height, boolean value) {
		int topLeftX = x / blockWidth;
		int topLeftY = y / blockHeight;
		int bottomRightX = (x + width) / blockWidth;
		int bottomRightY = (y + height) / blockHeight;
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
	
	public boolean isRectangleOpen(int x, int y, int width, int height) {
		int topLeftX = x / blockWidth;
		int topLeftY = y / blockHeight;
		int bottomRightX = (x + width) / blockWidth;
		int bottomRightY = (y + height) / blockHeight;
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
		int newX = x/blockWidth;
		int newY = y/blockHeight;
		if (newX >= 0 && newX < gridWidth && newY >= 0 && newY < gridHeight) {
			if (grid[newX][newY] == false)
				return true;
		}
		return false;
	}
	public boolean isBlockOpen(double x, double y) {
		return isBlockOpen((int) x, (int) y);
	}
	public boolean isBlockOpen(Point p) {
		return isBlockOpen(p.intX(), p.intY());
	}
	
	public boolean isOpen(int x, int y) { 
		if (grid[x][y] == false)
			return true;
		return false;
	}
	
	public int roundToGridX(double x) {
		return ((int) x / blockWidth) * blockWidth;
	}
	public int roundToGridY(double y) {
		return ((int) y / blockHeight) * blockHeight;
	}
	public Point roundToGrid(double x, double y) {
		return new Point(roundToGridX(x), roundToGridY(y));
	}
	public Point roundToGrid(Point p) {
		p.x = roundToGridX(p.x);
		p.y = roundToGridY(p.y);
		return p;
	}
}
