package triGame.game.versus;

import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import tSquare.game.entity.Entity;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.entities.zombies.ZombieSpawner;

class VersusSpawner extends ZombieSpawner {
	private final BossSpawner bossSpawners[] = new BossSpawner[2];
	private Entity[] bossTargets = null;
	private int bossSpawnZone = -1;
	
	VersusSpawner(ObjControllerI objController) {
		bossSpawners[0] = new BossSpawner(0, objController);
		bossSpawners[1] = new BossSpawner(1, objController);
	}
	
	@Override
	public void update(ZombieManager zombies) {
		super.update(zombies);

		bossSpawners[0].update(zombies);
		bossSpawners[1].update(zombies);
	}
	
	void spawnBoss() { bossSpawners[bossSpawnZone].spawnBoss(); }
	
	void setPlayerZoneAndTargets(int zone, Entity[] bossTargets) {
		this.bossTargets = bossTargets;
		bossSpawnZone = (zone == 0) ? 1 : 0;
	}
	
	private class BossSpawner {
		private static final long delayBetweenBossSpawns = 3000;
		private final int zone;
		private final NetVar.nInt bossToSpawn;
		private long nextSpawn = 0;
		
		private BossSpawner(int zone, ObjControllerI objController) {
			this.zone = zone;
			bossToSpawn = new NetVar.nInt(0, "zone" + zone + "BossToSpawn", objController);
			
		}
		
		private void update(ZombieManager zombies) {
			if (bossToSpawn.get() > 0 && System.currentTimeMillis() > nextSpawn) {
				zombies.createBoss(300, 40, bossTargets[zone]);
				bossToSpawn.set(bossToSpawn.get() - 1);
				nextSpawn = System.currentTimeMillis() + delayBetweenBossSpawns;
			}
		}
		
		private void spawnBoss() { bossToSpawn.set(bossToSpawn.get() + 1); }
	}	
}
