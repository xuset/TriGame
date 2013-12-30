package triGame.game.entities.dropPacks;

import objectIO.netObject.NetVar;
import objectIO.netObject.ObjControllerI;
import objectIO.netObject.NetVar.nInt;
import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;

public class DropPack extends Entity {
	private final long timeSpawned;

	private NetVar.nInt valueToGive;
	private NetVar.nInt timeToLive;
	

	DropPack(String sSpriteId, double startX, double startY,
			int valueToGive, int timeToLive) {
		
		super(sSpriteId, startX, startY);
		timeSpawned = System.currentTimeMillis();
		this.timeToLive.set(timeToLive);
		this.valueToGive.set(valueToGive);
	}
	
	DropPack(EntityKey key) {
		super(key);
		timeSpawned = System.currentTimeMillis();
	}
	
	@Override
	protected void setNetObjects(ObjControllerI objClass) {
		timeToLive = new nInt(0, "timeToLive", objClass);
		valueToGive = new nInt(0, "valueToGive", objClass);
	}
	
	public boolean isHealthPack() {
		return (spriteId.get().equals(HealthInfo.SPRITE_ID));
	}
	
	public boolean isPointPack() {
		return (spriteId.get().equals(PointInfo.SPRITE_ID));
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (timeSpawned + timeToLive.get() < System.currentTimeMillis())
			remove();
	}
	
	public int pickup() {
		if (!removeRequested()) {
			remove();
			return valueToGive.get();
		}
		return 0;
	}
	
	public static abstract class PackInfo {
		abstract int getValueToGive();
		abstract int getTimeToLive();
		abstract String getSpriteId();
		abstract boolean shouldDropPack();
	}
	
	public static class HealthInfo extends PackInfo{
		public static final String SPRITE_ID = "HealthPack";
		private static final int minHealth = 20;
		private static final int maxHealth = 60;
		private static final int maxTimeAlive = 35000;
		private static final int minTimeAlive = 20000;
		
		String getSpriteId() { return SPRITE_ID; }
		
		int getValueToGive() {
			return ((int) (Math.random() * (maxHealth - minHealth)) + minHealth);
		}
		
		int getTimeToLive() {
			return ((int) (Math.random() * (maxTimeAlive - minTimeAlive)) + minTimeAlive);
		}
		
		boolean shouldDropPack() { return (0.02 > Math.random()); }
	}
	
	public static class PointInfo extends PackInfo{
		public static final String SPRITE_ID = "PointPack";
		private static final int minPoints = 40;
		private static final int maxPoints = 120;
		private static final int maxTimeAlive = 15000;
		private static final int minTimeAlive = 6000;
		
		String getSpriteId() { return SPRITE_ID; }
		
		int getValueToGive() {
			return ((int) (Math.random() * (maxPoints - minPoints)) + minPoints);
		}
		
		int getTimeToLive() {
			return ((int) (Math.random() * (maxTimeAlive - minTimeAlive)) + minTimeAlive);
		}
		
		boolean shouldDropPack() { return (0.01 > Math.random()); }
	}

}
