package net.xuset.triGame.game.versus;

import java.util.ArrayDeque;
import java.util.Queue;

import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObj.NetClass;
import net.xuset.objectIO.netObj.NetFunc;
import net.xuset.objectIO.netObj.NetFuncListener;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.triGame.game.entities.zombies.ZombieManager;
import net.xuset.triGame.game.entities.zombies.ZombieSpawner;



class VersusSpawner extends ZombieSpawner {
	private final boolean isServer;
	private final BossSpawner bossSpawners[] = new BossSpawner[2];
	private Entity[] bossTargets = null;
	private int bossSpawnZone = 0;
	
	VersusSpawner(NetClass objController, boolean isServer) {
		bossSpawners[0] = new BossSpawner(0, objController);
		bossSpawners[1] = new BossSpawner(1, objController);
		this.isServer = isServer;
	}
	
	@Override
	public void update(ZombieManager zombies) {
		super.update(zombies);

		bossSpawners[0].update(zombies);
		bossSpawners[1].update(zombies);
	}
	
	void spawnBoss(double health, double speed) { bossSpawners[bossSpawnZone].spawnBoss(health, speed); }
	
	void setPlayerZoneAndTargets(int zone, Entity[] bossTargets) {
		this.bossTargets = bossTargets;
		bossSpawnZone = (zone == 0) ? 1 : 0;
	}
	
	private class BossSpawner {
		private static final int maxQueueSize = 20;
		private static final long delayBetweenBossSpawns = 3000;

		private final Queue<MarkupMsg> spawnQueue = new ArrayDeque<MarkupMsg>();
		private final int zone;
		private final NetFunc spawnFunc;
		private long nextSpawn = 0;
		
		private BossSpawner(int zone, NetClass objController) {
			this.zone = zone;
			spawnFunc = new NetFunc("zone" + zone + "SpawnFunc", new SpawnEvent());
			objController.addObj(spawnFunc);
			
		}
		
		private void update(ZombieManager zombies) {
			if (isServer && !spawnQueue.isEmpty() && System.currentTimeMillis() > nextSpawn) {
				MarkupMsg bossAttrib = spawnQueue.poll();
				int health = (int) bossAttrib.getAttribute("health").getDouble();
				double speed = bossAttrib.getAttribute("speed").getDouble();
				zombies.createBoss(health, speed, bossTargets[zone], VersusZombie.ZOMBIE_BUILDING_G);
				nextSpawn = System.currentTimeMillis() + delayBetweenBossSpawns;
			}
		}
		
		private void spawnBoss(double health, double speed) {
			MarkupMsg msg = new MarkupMsg();
			msg.addAttribute("speed", speed);
			msg.addAttribute("health", health);
			spawnFunc.sendCall(msg);
			if (isServer)
				addMsgToSpawnQueue(msg);
		}
		
		private void addMsgToSpawnQueue(MarkupMsg msg) {
			if (!isServer || spawnQueue.size() >= maxQueueSize)
				return;
			
			spawnQueue.add(msg);
		}
		
		private class SpawnEvent implements NetFuncListener {

			@Override
			public MarkupMsg funcCalled(MarkupMsg args) {
				addMsgToSpawnQueue(args);
				return null;
			}

			@Override
			public void funcReturned(MarkupMsg args) { }
			
		}
	}	
}
