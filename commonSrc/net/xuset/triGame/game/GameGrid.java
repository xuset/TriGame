package net.xuset.triGame.game;

public class GameGrid {
	private final int gridWidth, gridHeight;

	public int getGridWidth() { return gridWidth; }
	public int getGridHeight() { return gridHeight; }
	
	GameGrid(int gridWidth, int gridHeight) {
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
	}
	
	public boolean isInside(double x, double y) {
		return x >= 0 && y >= 0 && x < gridWidth && y < gridHeight;
	}
}
