package net.xuset.triGame.game.versus;

import java.util.Collection;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.game.entity.LocationCreator;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.tSquare.math.rect.IRectangleW;
import net.xuset.tSquare.math.rect.Rectangle;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.SpawnHole;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.buildings.BuildingCreator;
import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;
import net.xuset.triGame.game.entities.buildings.types.StrongWall;



final class VersusMap {
	static final int ZONE_SIZE = 20;
	static final int OFFSET_BLOCK_X = ZONE_SIZE;
	static final int OFFSET_BLOCK_Y = ZONE_SIZE;
	
	final IRectangleR missingWallZone, playableArea;
	final IRectangleW[] playables = new Rectangle[2];
	final Building[] headQuarters = new Building[2];
	
	private final IPointR[][] spawnLocations = new IPointR[2][3];
	private int zonesOnX, zonesOnY;
	
	VersusMap() {
		playableArea = new Rectangle(
				OFFSET_BLOCK_X,
				OFFSET_BLOCK_Y,
				ZONE_SIZE * 2,
				ZONE_SIZE * 1
		);
		
		missingWallZone = new Rectangle(
				(ZONE_SIZE - 1 + OFFSET_BLOCK_X),
				(6 + OFFSET_BLOCK_Y),
				2,
				2
		);
		
		createPlayables();
		
	}
	
	void findAndSetHq(Collection<Building> buildings) {
		int index = 0;
		for (Building b : buildings) {
			if (b.info == HeadQuarters.INFO)
				headQuarters[index++] = b;
		}
	}
	
	IPointR getSpawnForPlayer() {
		int x = (OFFSET_BLOCK_X + ZONE_SIZE - 1);
		int y = (VersusMap.OFFSET_BLOCK_Y + 6);
		if (Math.random() < 0.5)
			x += 1;
		return new Point(x, y);
	}
	
	private void createPlayables() {
		int y = OFFSET_BLOCK_Y;
		int xZone0 = OFFSET_BLOCK_X ;
		int xZone1 = (OFFSET_BLOCK_X + ZONE_SIZE);
		int width = ZONE_SIZE;
		
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
	
	IPointR[] getSpawns(int zoneNumber) {
		return spawnLocations[zoneNumber];
	}
	
	int getZoneNumber(Entity e) {
		return getZoneNumber(e.getX(), e.getY());
	}
	
	@SuppressWarnings("unused")
	int getZoneNumber(double x, double y) {
		return (x < (OFFSET_BLOCK_X + ZONE_SIZE)) ? 0 : 1;
	}
	
	void createMissingWalls(ManagerService managers) {
		BuildingCreator wall = managers.building.getCreator(StrongWall.INFO);
		
		int wallsX = (int) (missingWallZone.getWidth());
		int wallsY = (int) (missingWallZone.getHeight());
		int offsetX = ( (int) (missingWallZone.getX()) );
		int offsetY = ( (int) (missingWallZone.getY()) );
		
		for (int i = 0; i < wallsX; i++) {
			for (int j = 0; j < wallsY; j++) {
				int x = offsetX + i;
				int y = offsetY + j;
				
				Building toRemove = managers.building.getBuildingGetter().getByLocation(x, y);
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
			int x = location[0];
			int y = location[1];
			if ((teamNumber != 0 || count != 3) && (teamNumber != 1 || count != 2)) {
				creator.create(x, y, managers.spawnHole);
				spawnLocations[teamNumber][index] = new Point(x, y);
				index++;
			} else {
				if (teamNumber == 0)
					x -= 1;
				headQuarters[teamNumber] = managers.building.getCreator(HeadQuarters.INFO).create(x, y);
			}
			count++;
		}
	}
	
	private void wallZone(ManagerService managers, int blockX, int blockY) {
		BuildingCreator creator = managers.building.getCreator(StrongWall.INFO);
		final int rightWall =  (blockX + ZONE_SIZE - 1);
		final int bottomWall = (blockY + ZONE_SIZE - 1);
		final int leftWall = blockX;
		final int topWall =  blockY;
		
		for (int x = 0; x < ZONE_SIZE; x++) {
			createWall(creator, (blockX + x), topWall);
			createWall(creator, (blockX + x), bottomWall);			
		}
		for (int y = 1; y < ZONE_SIZE - 1; y++) {
			createWall(creator, leftWall, (blockY + y));
			createWall(creator, rightWall, (blockY + y));
		}
		
	}
	
	private Building createWall(BuildingCreator creator, double x, double y) {
		if (missingWallZone.contains(x, y))
			return null;
		return creator.create(x, y);
	}
}
