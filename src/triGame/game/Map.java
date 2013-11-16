package triGame.game;


import tSquare.math.Point;
import tSquare.paths.ObjectGrid;
import triGame.game.entities.PointWellManager;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.SpawnHoleManager;
import triGame.game.entities.buildings.BuildingCreator;
import triGame.game.entities.buildings.BuildingManager;
import triGame.game.entities.buildings.types.Barrier;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.safeArea.SafeAreaBoard;

abstract class Map {
	public static void createRandomMap(final ManagerService service, final SafeAreaBoard safeBoard) {
		service.building.getCreator(HeadQuarters.INFO).create(Params.GAME_WIDTH / 2 - 50, Params.GAME_HEIGHT / 2 - 50);
		
		createSpawnHoles(service.building, service.spawnHole, safeBoard);
		createPointWells(service.building, service.pointWell, service.spawnHole, safeBoard);
		wallEdges(service.building);
		placeRandomBarriers(service.building, service.pointWell, service.spawnHole);
	}
	
	private static void createSpawnHoles(BuildingManager buildingManger,
			SpawnHoleManager manager, SafeAreaBoard safeArea) {
		final int blockWidth = Params.BLOCK_SIZE;
		final int blockHeight = Params.BLOCK_SIZE;
		final int gameWidth = Params.GAME_WIDTH;
		final int gameHeight = Params.GAME_HEIGHT;
		
		manager.initialSpawnHoles = new SpawnHole[4];
		manager.initialSpawnHoles[0] = manager.create(gameWidth/2, gameHeight/2 + 500);
		manager.initialSpawnHoles[1] = manager.create(gameWidth/2, gameHeight/2 - 500);
		manager.initialSpawnHoles[2] = manager.create(gameWidth/2 + 500, gameHeight/2);
		manager.initialSpawnHoles[3] = manager.create(gameWidth/2 - 500, gameHeight/2);
		
		for (int i = 0; i < gameWidth * gameHeight / 250000 + 1; i++) {
			int x = (int) (Math.random() * (gameWidth - 2 * blockWidth)) + blockWidth;
			int y = (int) (Math.random() * (gameHeight - 2 * blockHeight)) + blockHeight;
			x = Params.roundToGrid(x);
			y = Params.roundToGrid(y);
			
			if (safeArea.insideSafeArea(x, y) == false && buildingManger.objectGrid.isBlockOpen(x, y))
				manager.create(x, y);
		}
	}
	
	private static void createPointWells(BuildingManager building, PointWellManager pointWell,
			SpawnHoleManager spawnHole, SafeAreaBoard safeBoard) {
		final int blockWidth = Params.BLOCK_SIZE;
		final int blockHeight = Params.BLOCK_SIZE;
		final int gameWidth = Params.GAME_WIDTH;
		final int gameHeight = Params.GAME_HEIGHT;
		for (int i = 0; i < gameWidth * gameHeight / 1000000; i++) {
			int x = (int) (Math.random() * (gameWidth - 2 * blockWidth)) + blockWidth;
			int y = (int) (Math.random() * (gameHeight - 2 * blockHeight)) + blockHeight;
			Point p = new Point(x, y);
			building.objectGrid.roundToGrid(p);
			if (safeBoard.insideSafeArea(p.intX(), p.intY()) == false && building.objectGrid.isBlockOpen(p) &&
					spawnHole.objectGrid.isBlockOpen(p) && pointWell.objectGrid.isBlockOpen(p))
				pointWell.create(p.intX(), p.intY());
		}
	}
	
	
	private static void wallEdges(BuildingManager manager) {
		final ObjectGrid objectGrid = manager.objectGrid;
		final int gridWidth = objectGrid.gridWidth;
		final int gridHeight = objectGrid.gridHeight;
		final int blockWidth = objectGrid.blockWidth;
		final int blockHeight = objectGrid.blockHeight;
		BuildingCreator creator = manager.getCreator(Barrier.INFO);
		
		manager.list.ensureCapacity(gridWidth * 2 + gridHeight * 2);
		
		for (int x = 0; x < gridWidth; x++) {
			
			if (objectGrid.isOpen(x, 0) && x > 2) {
				creator.create(x * blockWidth, 0);
			}
			
			if (objectGrid.isOpen(x, gridHeight - 1)) {
				creator.create(x * blockWidth, (gridHeight - 1) * blockHeight);
			}
			
		}
		for (int y = 1; y < gridHeight - 1 ; y++) {
			
			if (objectGrid.isOpen(0, y)&& y > 2) {
				creator.create(0, y*(blockHeight));
			}
			
			if (objectGrid.isOpen(gridWidth - 1, y)) {
				creator.create((gridWidth - 1) * blockWidth, y * blockHeight);
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
	
	private static void placeRandomBarriers(BuildingManager building,
			PointWellManager pointWell, SpawnHoleManager spawnHole) {
		int amount = Params.GAME_WIDTH * Params.GAME_HEIGHT / 20000;
		building.list.ensureCapacity(building.list.size() + amount);
		
		BuildingCreator barrier = building.getCreator(Barrier.INFO);
		
		for (int i = 0; i < amount; i++) {
			Point p = null;
			
			boolean foundSpot = false;
			while (foundSpot == false) {
				foundSpot = true;
				
				p = new Point(Math.random() * Params.GAME_WIDTH, Math.random() * Params.GAME_HEIGHT);
				Params.roundToGrid(p);
				
				if (building.objectGrid.isBlockOpen(p) == false ||
						building.objectGrid.isBlockOpen(p) == false||
						spawnHole.objectGrid.isBlockOpen(p) == false ||
						pointWell.objectGrid.isBlockOpen(p) == false)
					foundSpot = false;
				
				if (p.isEqualTo(Params.GAME_WIDTH / 2 - 50, Params.GAME_HEIGHT / 2 -100)) //location of player spawn
					foundSpot = false;
			}
			barrier.create(p.intX(),  p.intY());
		}
	}
}
