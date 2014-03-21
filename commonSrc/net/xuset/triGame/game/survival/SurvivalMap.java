package net.xuset.triGame.game.survival;


import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.paths.ObjectGrid;
import net.xuset.triGame.game.GameGrid;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.PointWellManager;
import net.xuset.triGame.game.entities.SpawnHole;
import net.xuset.triGame.game.entities.SpawnHoleManager;
import net.xuset.triGame.game.entities.buildings.BuildingCreator;
import net.xuset.triGame.game.entities.buildings.BuildingManager;
import net.xuset.triGame.game.entities.buildings.types.Barrier;
import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;
import net.xuset.triGame.game.survival.safeArea.SurvivalSafeBoard;


final class SurvivalMap {
	public static void createRandomMap(ManagerService service,
			SurvivalSafeBoard safeBoard, GameGrid gameGrid, double wallGenCoefficient) {
		
		final int centerX = gameGrid.getGridWidth() / 2;
		final int centerY = gameGrid.getGridHeight() / 2;
		service.building.getCreator(HeadQuarters.INFO).create(centerX - 1, centerY - 1);
		service.pointWell.create(centerX - 6, centerY - 6);
		
		createSpawnHoles(service.building, service.spawnHole, safeBoard, gameGrid);
		createPointWells(service.building, service.pointWell, service.spawnHole,
				safeBoard, gameGrid);
		wallEdges(service.building);
		placeRandomBarriers(service.building, service.pointWell, service.spawnHole,
				gameGrid, wallGenCoefficient);
	}
	
	private static void createSpawnHoles(BuildingManager buildingManger,
			SpawnHoleManager manager, SurvivalSafeBoard safeArea, GameGrid gameGrid) {
		final int gameWidth = gameGrid.getGridWidth();
		final int gameHeight = gameGrid.getGridHeight();
		
		manager.initialSpawnHoles = new SpawnHole[4];
		manager.initialSpawnHoles[0] = manager.create(gameWidth/2, gameHeight/2 + 10);
		manager.initialSpawnHoles[1] = manager.create(gameWidth/2, gameHeight/2 - 10);
		manager.initialSpawnHoles[2] = manager.create(gameWidth/2 + 10, gameHeight/2);
		manager.initialSpawnHoles[3] = manager.create(gameWidth/2 - 10, gameHeight/2);
		
		for (int i = 0; i < gameWidth * gameHeight / 100 + 1; i++) {
			int x = (int) (Math.random() * (gameWidth - 2)) + 1;
			int y = (int) (Math.random() * (gameHeight - 2)) + 1;
			
			if (safeArea.insideSafeArea(x, y, 1, 1) == false &&
					buildingManger.objectGrid.isBlockOpen(x, y))
				manager.create(x, y);
		}
	}
	
	private static void createPointWells(BuildingManager building,
			PointWellManager pointWell, SpawnHoleManager spawnHole,
			SurvivalSafeBoard safeBoard, GameGrid gameGrid) {
		
		final int gameWidth = gameGrid.getGridWidth();
		final int gameHeight = gameGrid.getGridHeight();
		for (int i = 0; i < gameWidth * gameHeight / 400; i++) {
			int x = (int) (Math.random() * (gameWidth - 2)) + 1;
			int y = (int) (Math.random() * (gameHeight - 2)) + 1;
			IPointR p = new Point(x, y);
			if (safeBoard.insideSafeArea((int) p.getX(), (int) p.getY(), 1, 1) == false &&
					building.objectGrid.isBlockOpen(p) &&
					spawnHole.objectGrid.isBlockOpen(p) &&
					pointWell.objectGrid.isBlockOpen(p))
				pointWell.create((int) p.getX(), (int) p.getY());
		}
	}
	
	
	private static void wallEdges(BuildingManager manager) {
		final ObjectGrid objectGrid = manager.objectGrid;
		final int gridWidth = objectGrid.gridWidth;
		final int gridHeight = objectGrid.gridHeight;
		BuildingCreator creator = manager.getCreator(Barrier.INFO);
		
		for (int x = 0; x < gridWidth; x++) {
			
			if (objectGrid.isOpen(x, 0) && x > 2) {
				creator.forceCreate(x, 0, manager);
			}
			
			if (objectGrid.isOpen(x, gridHeight - 1)) {
				creator.forceCreate(x, gridHeight - 1, manager);
			}
			
		}
		for (int y = 1; y < gridHeight - 1 ; y++) {
			
			if (objectGrid.isOpen(0, y)&& y > 2) {
				creator.forceCreate(0, y, manager);
			}
			
			if (objectGrid.isOpen(gridWidth - 1, y)) {
				creator.forceCreate(gridWidth - 1, y, manager);
			}
			
		}
	}
	
	private static void placeRandomBarriers(BuildingManager building,
			PointWellManager pointWell, SpawnHoleManager spawnHole,
			GameGrid gameGrid, double wallGenCoefficient) {
		
		final int gameWidth = gameGrid.getGridWidth();
		final int gameHeight = gameGrid.getGridHeight();
		
		int amount = (int) (gameWidth * gameHeight / 20.0 * wallGenCoefficient);
		
		BuildingCreator barrier = building.getCreator(Barrier.INFO);
		
		for (int i = 0; i < amount; i++) {
			IPointR p = null;
			
			boolean foundSpot = false;
			while (foundSpot == false) {
				foundSpot = true;
				
				p = new Point((int) (Math.random() * gameWidth), (int) (Math.random() * gameHeight));
				
				if (building.objectGrid.isBlockOpen(p) == false ||
						building.objectGrid.isBlockOpen(p) == false||
						spawnHole.objectGrid.isBlockOpen(p) == false ||
						pointWell.objectGrid.isBlockOpen(p) == false)
					foundSpot = false;
				
				if (p.equals(gameWidth / 2 - 1, gameHeight / 2 -2)) //location of player spawn
					foundSpot = false;
			}
			barrier.forceCreate((int) p.getX(), (int) p.getY(), building);
		}
	}
}
