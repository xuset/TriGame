package triGame.game.versus;

import java.util.ArrayDeque;
import java.util.Queue;

import objectIO.connections.Connection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjControllerI;
import tSquare.game.entity.Entity;
import triGame.game.entities.zombies.ZombieManager;
import triGame.game.entities.zombies.ZombieSpawner;

class VersusSpawner extends ZombieSpawner {
	private final boolean isServer;
	private final BossSpawner bossSpawners[] = new BossSpawner[2];
	private Entity[] bossTargets = null;
	private int bossSpawnZone = -1;
	
	VersusSpawner(ObjControllerI objController, boolean isServer) {
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
	
	void spawnBoss(int health, int speed) { bossSpawners[bossSpawnZone].spawnBoss(health, speed); }
	
	void setPlayerZoneAndTargets(int zone, Entity[] bossTargets) {
		this.bossTargets = bossTargets;
		bossSpawnZone = (zone == 0) ? 1 : 0;
	}
	
	private class BossSpawner {
		private static final int initialHealth = 300, initialSpeed = 40;
		private static final int maxQueueSize = 10;
		private static final long delayBetweenBossSpawns = 3000;

		private final Queue<MarkupMsg> spawnQueue = new ArrayDeque<MarkupMsg>();
		private final int zone;
		private final NetFunction spawnFunc;
		private long nextSpawn = 0;
		
		private BossSpawner(int zone, ObjControllerI objController) {
			this.zone = zone;
			spawnFunc = new NetFunction(objController, "zone" + zone + "SpawnFunc");
			spawnFunc.function = new SpawnEvent();
			
		}
		
		private void update(ZombieManager zombies) {
			if (isServer && !spawnQueue.isEmpty() && System.currentTimeMillis() > nextSpawn) {
				MarkupMsg bossAttrib = spawnQueue.poll();
				int health = initialHealth + bossAttrib.getAttribute("health").getInt();
				int speed = initialSpeed + bossAttrib.getAttribute("speed").getInt();
				zombies.createBoss(health, speed, bossTargets[zone]);
				nextSpawn = System.currentTimeMillis() + delayBetweenBossSpawns;
			}
		}
		
		private void spawnBoss(int health, int speed) {
			MarkupMsg msg = new MarkupMsg();
			msg.addAttribute(new MsgAttribute("speed").set(speed));
			msg.addAttribute(new MsgAttribute("health").set(health));
			spawnFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
			if (isServer)
				spawnFunc.function.calledFunc(msg, null);
		}
		
		private class SpawnEvent implements NetFunctionEvent {

			@Override
			public MarkupMsg calledFunc(MarkupMsg args, Connection c) {
				if (!isServer || spawnQueue.size() >= maxQueueSize)
					return null;
				
				spawnQueue.add(args);
				return null;
			}

			@Override
			public void returnedFunc(MarkupMsg args, Connection c) { }
			
		}
	}	
}
