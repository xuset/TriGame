package net.xuset.triGame.game.entities.dropPacks;

import net.xuset.tSquare.game.entity.Creator;
import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.game.entity.Manager;
import net.xuset.tSquare.game.entity.ManagerController;
import net.xuset.tSquare.game.entity.Creator.CreateFunc;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.entities.dropPacks.DropPack.PackInfo;
import net.xuset.triGame.game.shopping.ShopManager;


public class DropPackManager extends Manager<DropPack> {
	public static final String HASH_MAP_KEY = "dropPack";

	private final ShopManager shop;
	private final Creator<DropPack> creator;
	private final PackInfo packInfos[] = new PackInfo[] {
			new DropPack.HealthInfo(), 
			new DropPack.PointInfo()
		};

	
	public DropPackManager(ManagerController controller, ShopManager shop) {
		super(controller, HASH_MAP_KEY);
		this.shop = shop;
		creator = new Creator<DropPack>(HASH_MAP_KEY, controller.creator, new DropPackCreate());
	}
	
	public void maybeDropPack(double x, double y) {
		int whichOne = (int) (Math.random() * packInfos.length);
		PackInfo info = packInfos[whichOne];
		if (info.shouldDropPack())
			createNewDropPack(x, y, info);
	}
	
	public DropPack grabAnyPacks(IRectangleR rect) {
		for (DropPack dp : list) {
			if (rect.isInside(dp.hitbox)) {
				if (dp.isPointPack()) {
					int points = dp.pickup();
					shop.addPoints(points);
				}
				return dp;
			}
		}
		return null;
	}
	
	private void createNewDropPack(double x, double y, PackInfo info) {
		String spriteId = info.getSpriteId();
		Sprite sprite = Sprite.get(spriteId);
		double centerX = x - sprite.getWidth();
		double centerY = y - sprite.getHeight();
		int timeToLive = info.getTimeToLive();
		int valueToGive = info.getValueToGive();
		DropPack dp = new DropPack(spriteId, centerX, centerY, valueToGive, timeToLive);
		add(dp);
		creator.createOnNetwork(dp, this);
	}
	
	private class DropPackCreate implements CreateFunc<DropPack> {
		@Override
		public DropPack create(EntityKey key) {
			return new DropPack(key);
		}
	}
//	
//	private class DropPackCreator extends Creator<DropPack>{
//		private static final String classId = "dropPack";
//
//		public DropPackCreator(CreationHandler handler) {
//			super(classId, handler);
//		}	
//
//		@Override
//		protected DropPack parseMsg(MarkupMsg msg, EntityKey key) {
//			String spriteId = msg.getAttribute("spriteId").getString();
//			double x = msg.getAttribute("x").getDouble();
//			double y = msg.getAttribute("y").getDouble();
//			int valueToGive = msg.getAttribute("valueToGive").getInt();
//			long timeToLive = msg.getAttribute("timeToLive").getLong();
//			
//			return new DropPack(spriteId, x, y, valueToGive, timeToLive, key);
//			
//		}
//		
//		private DropPack create(String sSpriteId, double startX, double startY,
//				int valueToGive, long timeToLive) {
//			
//			MarkupMsg msg = new MarkupMsg();
//			msg.addAttribute(new MsgAttribute("spriteId").set(sSpriteId));
//			msg.addAttribute(new MsgAttribute("x").set(startX));
//			msg.addAttribute(new MsgAttribute("y").set(startY));
//			msg.addAttribute(new MsgAttribute("valueToGive").set(valueToGive));
//			msg.addAttribute(new MsgAttribute("timeToLive").set(timeToLive));
//			
//			EntityKey key = createUniqueKey(DropPackManager.this);
//			DropPack dropPack = new DropPack(sSpriteId, startX, startY,
//					valueToGive, timeToLive, key);
//			networkCreate(dropPack, key, msg);
//			localCreate(dropPack, DropPackManager.this);
//			return dropPack;
//		}
//		
//		protected DropPack create(double x, double y, PackInfo info) {
//			String spriteId = info.getSpriteId();
//			Sprite sprite = Sprite.get(spriteId);
//			double centeredX = x - sprite.getWidth() / 2;
//			double centeredY = y - sprite.getHeight() / 2;
//			return create(spriteId, centeredX, centeredY,
//					info.getReturnValue(), info.getTimeToLive());
//			
//		}
//		
//	}

}
