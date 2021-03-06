package net.xuset.triGame.game.entities.zombies;

import java.util.Collection;

import net.xuset.tSquare.math.point.PointR;



public class ZombieTargeter {
	private Collection<Zombie> zombies;
	public ZombieTargeter(Collection<Zombie> zombies) {
		this.zombies = zombies;
	}
	
	public ZombieTargeter() {
		zombies = null;
	}
	
	public void setDependencies(Collection<Zombie> zombies) {
		this.zombies = zombies;
	}
	
	public boolean canTargetZombies() { return !zombies.isEmpty(); }
	
	public Zombie targetZombie(double x, double y) {
		if (zombies.isEmpty())
			return null;
		
		Zombie closest = null;
		double shortestDist = 0.0;
		for (Zombie z : zombies) {
			double dist = PointR.distance(x, y, z.getCenterX(), z.getCenterY());
			if ((closest == null || dist < shortestDist) && isValidZombie(x, y, z)) {
				closest = z;
				shortestDist = dist;
			}
		}
		return closest;
	}
	
	@SuppressWarnings("unused")
	protected boolean isValidZombie(double x, double y, Zombie z) {
		return true;
	}
}
