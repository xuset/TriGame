package net.xuset.triGame.game.versus;

import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.buildings.Building;
import net.xuset.triGame.game.entities.zombies.Zombie;
import net.xuset.triGame.game.entities.zombies.ZombieHandler;


class VersusZombie extends ZombieHandler{
	static final int ZOMBIE_BUILDING_G = 80;
	private final VersusMap gameMap;
	private int spawnZone = -1;
	private boolean flop = false;
	
	VersusZombie(VersusMap gameMap, Observer<Integer> roundNumber) {
		super(roundNumber);
		this.gameMap = gameMap;
	}
	
	@Override
	protected IPointR setSpawnPoint(Entity target) {
		int zone = gameMap.getZoneNumber(target.getX(), target.getY());
		IPointR[] locations = gameMap.getSpawns(zone);
		int index = (int) (Math.random() * locations.length);
		return locations[index];
	}
	
	@Override
	protected Entity findTarget(Zombie z) {
		if (z == null) {
			spawnZone = (flop) ? 1 : 0;
			flop = !flop;
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
		if (a != null && b != null) {
			return (gameMap.getZoneNumber(a) == gameMap.getZoneNumber(b));
		} else if(a != null && spawnZone != -1) {
			return gameMap.getZoneNumber(a) == spawnZone;
		}
		System.err.println("Error in sameZone(Entity, Entity):VersusZombie.java. a is null and the spawn zone is not set");
		return false;
	}
	
	@Override
	protected int determinePathBuildingG() {
		return ZOMBIE_BUILDING_G;
	}
}
