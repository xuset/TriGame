package triGame.game.versus;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import tSquare.game.entity.Entity;
import tSquare.game.entity.LocationCreator;
import tSquare.math.Point;
import triGame.game.ManagerService;
import triGame.game.Params;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.buildings.BuildingCreator;
import triGame.game.entities.buildings.types.HeadQuarters;
import triGame.game.entities.buildings.types.StrongWall;

class VersusMap {
	static final int ZONE_SIZE = 20;
	static final int OFFSET_BLOCK_X = ZONE_SIZE;
	static final int OFFSET_BLOCK_Y = ZONE_SIZE;
	
	final Rectangle2D missingWallZone = new Rectangle();
	final Rectangle2D playableArea = new Rectangle();
	final Rectangle2D[] playables = new Rectangle2D[2];
	
	private final Point[][] spawnLocations = new Point[2][3];
	private final Building[] headQuarters = new Building[2];
	private int zonesOnX, zonesOnY;
	
	VersusMap() {
		
		playableArea.setFrame(
				OFFSET_BLOCK_X * Params.BLOCK_SIZE,
				OFFSET_BLOCK_Y * Params.BLOCK_SIZE,
				ZONE_SIZE * 2 * Params.BLOCK_SIZE,
				ZONE_SIZE * 1 * Params.BLOCK_SIZE
		);
		
		missingWallZone.setFrame(
				(ZONE_SIZE - 1 + OFFSET_BLOCK_X) * Params.BLOCK_SIZE,
				(6 + OFFSET_BLOCK_Y) * Params.BLOCK_SIZE,
				2 * Params.BLOCK_SIZE,
				2 * Params.BLOCK_SIZE
		);
		
		createPlayables();
	}
	
	void createPlayables() {
		int y = OFFSET_BLOCK_Y * Params.BLOCK_SIZE;
		int xZone0 = OFFSET_BLOCK_X * Params.BLOCK_SIZE;
		int xZone1 = (OFFSET_BLOCK_X + ZONE_SIZE) * Params.BLOCK_SIZE;
		int width = ZONE_SIZE * Params.BLOCK_SIZE;
		
		playables[0] = new Rectangle();
		playables[1] = new Rectangle();
		playables[0].setFrame(xZone0, y, width, width);
		playables[1].setFrame(xZone1, y, width, width);
	}
	
	boolean isZoneHQDead(int zone, ManagerService managers) {

		for (Building b : managers.building.interactives) {
			if (b.info == HeadQuarters.INFO &&
					getZoneNumber(b.getX(), b.getY()) == zone) {
				
				return false;
			}
		}
		return true;
	}
	
	Point[] getSpawns(int zoneNumber) {
		return spawnLocations[zoneNumber];
	}
	
	int getZoneNumber(Entity e) {
		return getZoneNumber(e.getX(), e.getY());
	}
	
	int getZoneNumber(double x, double y) {
		return (x < (OFFSET_BLOCK_X + ZONE_SIZE) * Params.BLOCK_SIZE) ? 0 : 1;
	}
	
	void createMissingWalls(ManagerService managers) {
		BuildingCreator wall = managers.building.getCreator(StrongWall.INFO);
		
		int wallsX = (int) (missingWallZone.getWidth() / Params.BLOCK_SIZE);
		int wallsY = (int) (missingWallZone.getHeight() / Params.BLOCK_SIZE);
		int offsetX = Params.roundToGrid(missingWallZone.getX());
		int offsetY = Params.roundToGrid(missingWallZone.getY());
		
		for (int i = 0; i < wallsX; i++) {
			for (int j = 0; j < wallsY; j++) {
				int x = offsetX + i * Params.BLOCK_SIZE;
				int y = offsetY + j * Params.BLOCK_SIZE;
				
				Building toRemove = managers.building.getByLocation(x, y);
				if (toRemove != null) {
					toRemove.remove();
					managers.building.updateList();
				}
				wall.forceCreate(x, y, managers.building);
			}
		}
	}
	
	void createMap(ManagerService managers) {
		zonesOnY = 1;
		zonesOnX = 2;
		createZones(managers);
	}
	
	private void createZones(ManagerService managers) {
		int count = 0;
		for (int x = 0; x < zonesOnX; x++) {
			for (int y = 0; y < zonesOnY; y++) {
				int blockX = x * ZONE_SIZE + OFFSET_BLOCK_X;
				int blockY = y * ZONE_SIZE + OFFSET_BLOCK_Y;
				wallZone(managers, blockX, blockY);
				createZoneSpawns(managers, blockX, blockY, count);
				count++;
			}
		}
	}
	
	private void createZoneSpawns(ManagerService managers,
			int blockX, int blockY, int teamNumber) {
		
		final int spawns[][] = new int[][] {
				{blockX + ZONE_SIZE / 2, blockY + 1},
				{blockX + ZONE_SIZE / 2, blockY + ZONE_SIZE - 2},
				{blockX + 1, blockY + ZONE_SIZE / 2},
				{blockX + ZONE_SIZE - 2, blockY + ZONE_SIZE/2}
		};
		
		int index = 0;
		int count = 0;
		LocationCreator<SpawnHole> creator = managers.spawnHole.creator;
		for (int[] location : spawns) {
			int x = location[0] * Params.BLOCK_SIZE;
			int y = location[1] * Params.BLOCK_SIZE;
			if ((teamNumber != 0 || count != 3) && (teamNumber != 1 || count != 2)) {
				creator.create(x, y, managers.spawnHole);
				spawnLocations[teamNumber][index] = new Point(x, y);
				index++;
			} else {
				if (teamNumber == 0)
					x -= Params.BLOCK_SIZE;
				headQuarters[teamNumber] = managers.building.getCreator(HeadQuarters.INFO).create(x, y);
			}
			count++;
		}
	}
	
	private void wallZone(ManagerService managers, int blockX, int blockY) {
		BuildingCreator creator = managers.building.getCreator(StrongWall.INFO);
		final int rightWall =  (blockX + ZONE_SIZE - 1) * Params.BLOCK_SIZE;
		final int bottomWall = (blockY + ZONE_SIZE - 1) * Params.BLOCK_SIZE;
		final int leftWall = blockX * Params.BLOCK_SIZE;
		final int topWall =  blockY * Params.BLOCK_SIZE;
		
		for (int x = 0; x < ZONE_SIZE; x++) {
			createWall(creator, (blockX + x) * Params.BLOCK_SIZE, topWall);
			createWall(creator, (blockX + x) * Params.BLOCK_SIZE, bottomWall);			
		}
		for (int y = 1; y < ZONE_SIZE - 1; y++) {
			createWall(creator, leftWall, (blockY + y) * Params.BLOCK_SIZE);
			createWall(creator, rightWall, (blockY + y) * Params.BLOCK_SIZE);
		}
		
	}
	
	private Building createWall(BuildingCreator creator, double x, double y) {
		if (missingWallZone.contains(x, y))
			return null;
		return creator.create(x, y);
	}
}
