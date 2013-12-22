package triGame.game.entities.zombies;

public final class ZombieSpawner {
	private int zombiesToSpawn;
	private long nextSpawnTime;
	private long spawnDelta;
	private boolean spawnBoss;
	
	public void startNewSpawnRound(int zombiesToSpawn, long spawnDelta, boolean boss) {
		this.zombiesToSpawn = zombiesToSpawn;
		this.spawnDelta = spawnDelta;
	}
	
	public boolean finishedSpawn() { return zombiesToSpawn <= 0; }
	
	public void update(ZombieManager zombies) {
		if (zombiesToSpawn > 0 && System.currentTimeMillis() >= nextSpawnTime &&
				zombies.getZombiesAlive() < Zombie.MAX_ZOMBIES) {
			if (spawnBoss)
				zombies.createBoss();
			else
				zombies.create();
			zombiesToSpawn--;
			nextSpawnTime = System.currentTimeMillis() + spawnDelta;
		}		
	}
}
