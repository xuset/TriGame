package net.xuset.triGame.game.versus;

import java.util.ArrayDeque;
import java.util.Queue;

import net.xuset.objectIO.connections.Connection;
import net.xuset.objectIO.connections.ConnectionI;
import net.xuset.objectIO.markupMsg.MarkupMsg;
import net.xuset.objectIO.netObject.NetFunction;
import net.xuset.objectIO.netObject.NetFunctionEvent;
import net.xuset.objectIO.netObject.NetObjUpdater;
import net.xuset.tSquare.game.entity.Entity;
import net.xuset.triGame.game.entities.zombies.ZombieManager;
import net.xuset.triGame.game.entities.zombies.ZombieSpawner;



class VersusSpawner extends ZombieSpawner {
	private final boolean isServer;
	private final BossSpawner bossSpawners[] = new BossSpawner[2];
	private Entity[] bossTargets = null;
	private int bossSpawnZone = 0;
	
	VersusSpawner(NetObjUpdater objController, boolean isServer) {
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
		private final NetFunction spawnFunc;
		private long nextSpawn = 0;
		
		private BossSpawner(int zone, NetObjUpdater objController) {
			this.zone = zone;
			spawnFunc = new NetFunction(objController, "zone" + zone + "SpawnFunc");
			spawnFunc.function = new SpawnEvent();
			
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
			spawnFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
			if (isServer)
				spawnFunc.function.calledFunc(msg, null);
		}
		
		private class SpawnEvent implements NetFunctionEvent {

			@Override
			public MarkupMsg calledFunc(MarkupMsg args, ConnectionI c) {
				if (!isServer || spawnQueue.size() >= maxQueueSize)
					return null;
				
				spawnQueue.add(args);
				return null;
			}

			@Override
			public void returnedFunc(MarkupMsg args, ConnectionI c) { }
			
		}
	}	
}
