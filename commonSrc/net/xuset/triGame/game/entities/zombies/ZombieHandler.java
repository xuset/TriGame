package net.xuset.triGame.game.entities.zombies;

import java.util.Collection;
import java.util.List;

import net.xuset.tSquare.game.GameIntegratable;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.math.point.IPointR;
import net.xuset.tSquare.math.point.IPointW;
import net.xuset.tSquare.math.point.Point;
import net.xuset.tSquare.math.point.PointR;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.ManagerService;
import net.xuset.triGame.game.entities.Person;
import net.xuset.triGame.game.entities.SpawnHole;
import net.xuset.triGame.game.entities.buildings.Building;



public class ZombieHandler implements GameIntegratable{	
	protected ManagerService managers;
	protected int roundNumber = 0;
	
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
	
	private static final int maxSpawnDistance = 12;
	private static final int maxBufferSize = 15;
	protected IPointR setSpawnPoint(Entity target) {

		final float width = Sprite.get(Zombie.SPRITE_ID).getWidth();
		final float height = Sprite.get(Zombie.SPRITE_ID).getHeight();
		
		if (target == null) {
			System.err.println("Zombie's target is null.");
			return new Point(0,0);
		}
		
		List<SpawnHole> spawnHoles = managers.spawnHole.list;
		SpawnHole[] buffer = new SpawnHole[maxBufferSize];
		
		int size = 0;
		for (SpawnHole sh : spawnHoles) {
			int distance = (int) PointR.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
			if (distance < maxSpawnDistance && size < maxBufferSize) {
				buffer[size] = sh;
				size++;
			}
		}
		if (size == 0) {
			SpawnHole[] initials = managers.spawnHole.initialSpawnHoles;
			SpawnHole shortest = initials[0];
			int shortestDistance = (int) PointR.distance(shortest.getX(), shortest.getY(), target.getX(), target.getY());
			for (SpawnHole sh : initials) {
				int distance = (int) PointR.distance(sh.getX(), sh.getY(), target.getX(), target.getY());
				if (distance < shortestDistance) {
					shortest = sh;
					shortestDistance = distance;
				}
			}
			IPointW p = new Point(shortest.getCenter());
			p.translate(-width/2, -height/2);
			return p;
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

	@SuppressWarnings("unused")
	protected boolean isBuildingValid(Building b, Zombie z) {
		return true;
	}

	@SuppressWarnings("unused")
	protected boolean isPersonValid(Person p, Zombie z) {
		return (!p.isDead() && !p.removeRequested());
	}
	
	protected int determineHealth() {
		int players = managers.person.list.size();
		int health = (int) ((roundNumber * roundNumber / 10.0)  + (players * roundNumber * 0.5) + 90.0);
		return health;
	}
	
	protected double determineSpeed() {
		double speed = (int) (50 + 0.25 * roundNumber * roundNumber);
		speed = (speed > 400) ? 400 : speed;
		speed /= 50.0;
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
	public void update(int frameDelta) {
		
	}

	@Override
	public void draw(IGraphics g) {
		
	}
	
}
