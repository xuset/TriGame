package triGame.game;


import tSquare.game.GameBoard;
import tSquare.math.Point;
import tSquare.paths.ObjectGrid;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.SpawnHoleManager;
import triGame.game.entities.building.BuildingManager;
import triGame.game.entities.wall.Barrier;
import triGame.game.entities.wall.WallManager;
import triGame.game.safeArea.SafeAreaBoard;

abstract class Map {
	public static void createRandomMap(WallManager wallManager, BuildingManager buildingManager, SpawnHoleManager spawnManager, SafeAreaBoard safeArea) {
		createSpawnHoles(buildingManager, spawnManager, safeArea);
		wallEdges(wallManager);
		placeRandomBarriers(wallManager, buildingManager, spawnManager, safeArea);
	}
	
	private static void createSpawnHoles(BuildingManager buildingManger, SpawnHoleManager manager, SafeAreaBoard safeArea) {
		final int blockWidth = manager.objectGrid.getBlockWidth();
		final int blockHeight = manager.objectGrid.getBlockHeight();
		final int gameWidth = manager.gameBoard.getWidth();
		final int gameHeight = manager.gameBoard.getHeight();
		
		manager.initialSpawnHoles = new SpawnHole[4];
		manager.initialSpawnHoles[0] = manager.create(gameWidth/2, gameHeight/2 + 500);
		manager.initialSpawnHoles[1] = manager.create(gameWidth/2, gameHeight/2 - 500);
		manager.initialSpawnHoles[2] = manager.create(gameWidth/2 + 500, gameHeight/2);
		manager.initialSpawnHoles[3] = manager.create(gameWidth/2 - 500, gameHeight/2);
		
		for (int i = 0; i < gameWidth * gameHeight / 250000 + 1; i++) {
			int x = (int) (Math.random() * (gameWidth - 2 * blockWidth)) + blockWidth;
			int y = (int) (Math.random() * (gameHeight - 2 * blockHeight)) + blockHeight;
			x = manager.objectGrid.roundToGridX(x);
			y = manager.objectGrid.roundToGridX(y);
			
			if (safeArea.insideSafeArea(x, y) == false && buildingManger.objectGrid.isBlockOpen(x, y))
				manager.create(x, y);
		}
	}
	
	private static void wallEdges(WallManager manager) {
		final ObjectGrid objectGrid = manager.objectGrid;
		final int gridWidth = objectGrid.gridWidth;
		final int gridHeight = objectGrid.gridHeight;
		final int blockWidth = objectGrid.blockWidth;
		final int blockHeight = objectGrid.blockHeight;
		
		manager.getList().ensureCapacity(gridWidth * 2 + gridHeight * 2);
		
		for (int x = 0; x < gridWidth; x++) {
			
			if (objectGrid.isOpen(x, 0) && x > 2) {
				Barrier.create(x * blockWidth, 0, manager);
			}
			
			if (objectGrid.isOpen(x, gridHeight - 1)) {
				Barrier.create(x * blockWidth, (gridHeight - 1) * blockHeight, manager);
			}
			
		}
		for (int y = 1; y < gridHeight - 1 ; y++) {
			
			if (objectGrid.isOpen(0, y)&& y > 2) {
				Barrier.create(0, y*(blockHeight), manager);
			}
			
			if (objectGrid.isOpen(gridWidth - 1, y)) {
				Barrier.create((gridWidth - 1) * blockWidth, y * blockHeight, manager);
			}
			
		}
	}
	
	/*private static void wallCircle(BuildingManager manager, int radius) {
		int centerX = manager.gameBoard.getWidth() / 2;
		int centerY = manager.gameBoard.getHeight() / 2;
		for (int x = -1 * radius + centerX; x <= radius + centerX; x += 50) {
			int y = circleFunction(x, centerX, radius) + centerY;
			if (manager.objectGrid.isBlockOpen(x, y))
				Barrier.create(x, y, manager);
			y = -1 * circleFunction(x, centerX, radius) + centerY;
			if (manager.objectGrid.isBlockOpen(x, y))
				Barrier.create(x, y, manager);
		}
	}
	
	private static int circleFunction(int x, int offsetX, int radius) {
		int r2 = radius * radius;
		int x2 = (x - offsetX) * (x - offsetX);
		int sum = r2 - x2;
		int root = (int) Math.sqrt(sum);
		return (root / 50) * 50;
		//return ((int) Math.sqrt((x - offsetX) * (x - offsetX) - (radius * radius)));
	}*/
	
	private static void placeRandomBarriers(WallManager wallManager, BuildingManager buildingManager, SpawnHoleManager spawnManager, SafeAreaBoard safeArea) {
		GameBoard gameBoard = buildingManager.gameBoard;
		int amount = gameBoard.getWidth() * gameBoard.getHeight() / 20000;
		wallManager.getList().ensureCapacity(wallManager.getList().size() + amount);
		
		for (int i = 0; i < amount; i++) {
			Point p = null;
			
			boolean foundSpot = false;
			while (foundSpot == false) {
				foundSpot = true;
				
				p = wallManager.objectGrid.roundToGrid(Math.random() * gameBoard.getWidth(), Math.random() * gameBoard.getHeight());
				if (wallManager.objectGrid.isBlockOpen(p) == false || buildingManager.objectGrid.isBlockOpen(p) == false || spawnManager.objectGrid.isBlockOpen(p) == false)
					foundSpot = false;
			}
			Barrier.create(p.intX(),  p.intY(), wallManager);
		}
	}
}
