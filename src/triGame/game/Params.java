package triGame.game;

import tSquare.math.Point;

public final class Params {
	public static final String VERSION = "1.1.0";
	public static final int BLOCK_SIZE = 50;
	public static final int GAME_WIDTH = BLOCK_SIZE * 100;
	public static final int GAME_HEIGHT = BLOCK_SIZE * 100;
	
	
	public static int roundToGrid(double x) { return ((int) (x / BLOCK_SIZE)) * BLOCK_SIZE; }
	
	public static Point roundToGrid(Point p) { p.x = roundToGrid(p.x); p.y = roundToGrid(p.y); return p;}
	
	public static boolean isInsideGame(double x, double y) {
		return (x >= 0 && x < GAME_WIDTH && y >= 0 && y < GAME_HEIGHT);
	}
}
