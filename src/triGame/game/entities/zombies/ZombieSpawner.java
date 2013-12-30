package triGame.game.entities.zombies;

public class ZombieSpawner {
	private int zombiesToSpawn;
	private long nextSpawnTime;
	private long spawnDelta;
	private boolean spawnBoss;
	
	public void startNewSpawnRound(int zombiesToSpawn, long spawnDelta, boolean spawnBoss) {
		this.zombiesToSpawn = zombiesToSpawn;
		this.spawnDelta = spawnDelta;
		this.spawnBoss = spawnBoss;
	}
	
	public boolean finishedSpawn() { return zombiesToSpawn <= 0; }
	
	public void update(ZombieManager zombies) {
		if (zombiesToSpawn > 0 && System.currentTimeMillis() >= nextSpawnTime &&
				zombies.getZombiesAlive() < Zombie.MAX_ZOMBIES) {
			if (spawnBoss)
				zombies.createBoss();
			else
				zombies.createZombie();
			zombiesToSpawn--;
			nextSpawnTime = System.currentTimeMillis() + spawnDelta;
		}		
	}
}
