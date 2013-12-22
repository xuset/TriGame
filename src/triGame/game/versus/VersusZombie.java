package triGame.game.versus;

import tSquare.game.entity.Entity;
import tSquare.math.Point;
import tSquare.util.Observer;
import triGame.game.entities.Person;
import triGame.game.entities.buildings.Building;
import triGame.game.entities.zombies.Zombie;
import triGame.game.entities.zombies.ZombieHandler;

class VersusZombie extends ZombieHandler{
	private final VersusMap gameMap;
	private int spawnZone = -1;
	
	VersusZombie(VersusMap gameMap, Observer<Integer> roundNumber) {
		super(roundNumber);
		this.gameMap = gameMap;
	}
	
	@Override
	protected Point setSpawnPoint(Entity target) {
		int zone = gameMap.getZoneNumber(target.getX(), target.getY());
		Point[] locations = gameMap.getSpawns(zone);
		int index = (int) (Math.random() * locations.length);
		return locations[index];
	}
	
	@Override
	protected Entity findTarget(Zombie z) {
		if (z == null) {
			spawnZone = (int) (Math.random() * 2);
		}
		Entity target =  super.findTarget(z);
		spawnZone = -1;
		return target;
	}
	
	@Override
	protected boolean isPersonValid(Person p, Zombie z) {
		return sameZone(p, z) && super.isPersonValid(p, z);
	}
	
	@Override
	protected boolean isBuildingValid(Building b, Zombie z) {
		return sameZone(b, z) && super.isBuildingValid(b, z);
	}
	
	private boolean sameZone(Entity a, Entity b) {
		int aZone = gameMap.getZoneNumber(a);
		if (spawnZone != -1)
			return spawnZone == aZone;
		
		return (gameMap.getZoneNumber(a) == gameMap.getZoneNumber(b));
	}
	
	@Override
	protected int determinePathBuildingG() {
		return 80;
	}
}
