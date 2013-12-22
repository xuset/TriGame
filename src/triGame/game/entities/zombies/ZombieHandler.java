package triGame.game.entities.zombies;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import tSquare.game.GameBoard.ViewRect;
import tSquare.game.GameIntegratable;
import tSquare.game.entity.Entity;
import tSquare.imaging.Sprite;
import tSquare.math.Point;
import tSquare.util.Observer;
import triGame.game.ManagerService;
import triGame.game.entities.Person;
import triGame.game.entities.SpawnHole;
import triGame.game.entities.buildings.Building;

public class ZombieHandler implements GameIntegratable{	
	protected ManagerService managers;
	protected int roundNumber = 0;
	
	//public ZombieHandler() {
		
	//}
	
	public ZombieHandler(Observer<Integer> roundNumber) {
		new RoundObserver(roundNumber);
	}
	
	public void setDependencies(ManagerService managers) {
		this.managers = managers;
	}
	
	private class RoundObserver implements Observer.Change<Integer> {
		RoundObserver(Observer<Integer> ob) {
			ob.watch(this);
		}

		@Override
		public void observeChange(Integer t) {
			roundNumber = t;
		}
	}
	
	private static final int maxSpawnDistance = 600;
	private static final int maxBufferSize = 15;
	protected Point setSpawnPoint(Entity target) {

		final int width = Sprite.get(Zombie.SPRITE_ID).getWidth();
		final int height = Sprite.get(Zombie.SPRITE_ID).getHeight();
		
		if (target == null) {
			System.err.println("Zombie's target is null.");
			return new Point(0,0);
		}
		
		ArrayList<SpawnHole> spawnHoles = managers.spawnHole.list;
		SpawnHole[] buffer = new SpawnHole[maxBufferSize];
		
		int size = 0;
		for (SpawnHole sh : spawnHoles) {
			int distance = (int) Point.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
			if (distance < maxSpawnDistance && size < maxBufferSize) {
				buffer[size] = sh;
				size++;
			}
		}
		if (size == 0) {
			SpawnHole[] initials = managers.spawnHole.initialSpawnHoles;
			SpawnHole shortest = initials[0];
			int shortestDistance = (int) Point.distance(shortest.getX(), shortest.getY(), target.getX(), target.getY());
			for (SpawnHole sh : initials) {
				int distance = (int) Point.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
				if (distance < shortestDistance) {
					shortest = sh;
					shortestDistance = distance;
				}
			}
			return new Point(shortest.getCenter()).translate(-width/2, -height/2);
		} else {
			SpawnHole sh;
			double rand = Math.random();
			double index = size * rand;
			sh = buffer[(int) index];
			return new Point(sh.getCenterX() - width/2, sh.getCenterY() - height/2);
		}
	}

	protected static final int personWeight = 50;
	protected Entity findTarget(Zombie z) {
		Collection<Building> buildings = managers.building.interactives;
		Collection<Person> persons = managers.person.list;
		int totalWeights = sumAllWeights(z, buildings, persons);
		int selected = (int) (Math.random() * totalWeights);
		int sum = 0;
		
		for (Person p : persons) {
			if (isPersonValid(p, z))
				sum += personWeight;
			if (sum > selected)
				return p;
		}
		for (Building b : buildings) {
			if (isBuildingValid(b, z))
				sum += b.info.selectionWeight;
			if (sum > selected)
				return b;
		}
		return null;
	}
	
	private int sumAllWeights(Zombie z, Collection<Building> buildings, Collection<Person> persons) {
		int totalWeights = 0;
		for (Person p : persons) {
			if (isPersonValid(p, z))
				totalWeights += personWeight;
		}
		for (Building b : buildings) {
			if (isBuildingValid(b, z))
				totalWeights += b.info.selectionWeight;
		}
		return totalWeights;
	}
	
	protected boolean isBuildingValid(Building b, Zombie z) {
		return true;
	}
	
	protected boolean isPersonValid(Person p, Zombie z) {
		return (!p.isDead() && !p.removeRequested());
	}
	
	protected int determineHealth() {
		int players = managers.person.list.size();
		int health = (int) ((roundNumber * roundNumber / 10.0)  + (players * roundNumber * 1.0) + 90.0);
		return health;
	}
	
	protected int determineSpeed() {
		int speed = (int) (50 + 0.25 * roundNumber * roundNumber);
		speed = (speed > 400) ? 400 : speed;
		return speed;
	}
	
	protected int determineSpawnDelay() {
		int spawnDelay = 3000 - 75 * roundNumber;
		spawnDelay = (spawnDelay < 0) ? 0 : spawnDelay;
		return spawnDelay;
	}
	
	protected int determinePathBuildingG() {
		return 220;
	}

	@Override
	public void performLogic(int frameDelta) {
		
	}

	@Override
	public void draw(Graphics2D g, ViewRect rect) {
		
	}
	
}
