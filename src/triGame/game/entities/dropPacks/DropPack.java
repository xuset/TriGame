package triGame.game.entities.dropPacks;

import tSquare.game.entity.Entity;
import tSquare.game.entity.EntityKey;

public class DropPack extends Entity {	
	private final long timeToLive;
	private final int valueToGive;
	private final long timeSpawned;

	public DropPack(String sSpriteId, double startX, double startY,
			int valueToGive, long timeToLive, EntityKey key) {
		
		super(sSpriteId, startX, startY, key);
		this.timeToLive = timeToLive;
		this.valueToGive = valueToGive;
		timeSpawned = System.currentTimeMillis();
	}
	
	public boolean isHealthPack() {
		return (spriteId.get().equals(HealthInfo.SPRITE_ID));
	}
	
	public boolean isPointPack() {
		return (spriteId.get().equals(PointInfo.SPRITE_ID));
	}
	
	@Override
	public void performLogic(int frameDelta) {
		if (timeSpawned + timeToLive < System.currentTimeMillis())
			remove();
	}
	
	public int pickup() {
		if (!removeRequested()) {
			remove();
			return valueToGive;
		}
		return 0;
	}
	
	public static abstract class PackInfo {
		abstract int getReturnValue();
		abstract long getTimeToLive();
		abstract String getSpriteId();
		abstract boolean shouldDropPack();
	}
	
	public static class HealthInfo extends PackInfo{
		public static final String SPRITE_ID = "HealthPack";
		private static final int minHealth = 20;
		private static final int maxHealth = 60;
		private static final long maxTimeAlive = 35000;
		private static final long minTimeAlive = 20000;
		
		String getSpriteId() { return SPRITE_ID; }
		
		int getReturnValue() {
			return ((int) (Math.random() * (maxHealth - minHealth)) + minHealth);
		}
		
		long getTimeToLive() {
			return ((long) (Math.random() * (maxTimeAlive - minTimeAlive)) + minTimeAlive);
		}
		
		boolean shouldDropPack() { return (0.02 > Math.random()); }
	}
	
	public static class PointInfo extends PackInfo{
		public static final String SPRITE_ID = "PointPack";
		private static final int minPoints = 40;
		private static final int maxPoints = 120;
		private static final long maxTimeAlive = 15000;
		private static final long minTimeAlive = 6000;
		
		String getSpriteId() { return SPRITE_ID; }
		
		int getReturnValue() {
			return ((int) (Math.random() * (maxPoints - minPoints)) + minPoints);
		}
		
		long getTimeToLive() {
			return ((long) (Math.random() * (maxTimeAlive - minTimeAlive)) + minTimeAlive);
		}
		
		boolean shouldDropPack() { return (0.01 > Math.random()); }
	}

}
