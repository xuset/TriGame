package triGame.game.entities.dropPacks;

import java.awt.geom.Rectangle2D;

import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import tSquare.game.entity.CreationHandler;
import tSquare.game.entity.Creator;
import tSquare.game.entity.EntityKey;
import tSquare.game.entity.Manager;
import tSquare.game.entity.ManagerController;
import tSquare.imaging.Sprite;
import triGame.game.entities.dropPacks.DropPack.PackInfo;
import triGame.game.shopping.ShopManager;

public class DropPackManager extends Manager<DropPack> {
	public static final String HASH_MAP_KEY = "dropPack";

	private final ShopManager shop;
	private final DropPackCreator creator;
	private final PackInfo packInfos[] = new PackInfo[] {
			new DropPack.HealthInfo(), 
			new DropPack.PointInfo()
		};

	
	public DropPackManager(ManagerController controller, ShopManager shop) {
		super(controller, HASH_MAP_KEY);
		this.shop = shop;
		creator = new DropPackCreator(controller.creator);
	}
	
	public void maybeDropPack(double x, double y) {
		int whichOne = (int) (Math.random() * packInfos.length);
		PackInfo info = packInfos[whichOne];
		if (info.shouldDropPack())
			creator.create(x, y, info);
	}
	
	public DropPack grabAnyPacks(Rectangle2D rect) {
		for (DropPack dp : list) {
			if (rect.intersects(dp.hitbox) || rect.contains(dp.hitbox)) {
				if (dp.isPointPack()) {
					int points = dp.pickup();
					shop.addPoints(points);
				}
				return dp;
			}
		}
		return null;
	}
	
	private class DropPackCreator extends Creator<DropPack>{
		private static final String classId = "dropPack";

		public DropPackCreator(CreationHandler handler) {
			super(classId, handler);
		}	

		@Override
		protected DropPack parseMsg(MarkupMsg msg, EntityKey key) {
			String spriteId = msg.getAttribute("spriteId").getString();
			double x = msg.getAttribute("x").getDouble();
			double y = msg.getAttribute("y").getDouble();
			int valueToGive = msg.getAttribute("valueToGive").getInt();
			long timeToLive = msg.getAttribute("timeToLive").getLong();
			
			return new DropPack(spriteId, x, y, valueToGive, timeToLive, key);
			
		}
		
		private DropPack create(String sSpriteId, double startX, double startY,
				int valueToGive, long timeToLive) {
			
			MarkupMsg msg = new MarkupMsg();
			msg.addAttribute(new MsgAttribute("spriteId").set(sSpriteId));
			msg.addAttribute(new MsgAttribute("x").set(startX));
			msg.addAttribute(new MsgAttribute("y").set(startY));
			msg.addAttribute(new MsgAttribute("valueToGive").set(valueToGive));
			msg.addAttribute(new MsgAttribute("timeToLive").set(timeToLive));
			
			EntityKey key = getNewKey(DropPackManager.this);
			DropPack dropPack = new DropPack(sSpriteId, startX, startY,
					valueToGive, timeToLive, key);
			localCreate(dropPack, DropPackManager.this);
			networkCreate(key, msg);
			return dropPack;
		}
		
		protected DropPack create(double x, double y, PackInfo info) {
			String spriteId = info.getSpriteId();
			Sprite sprite = Sprite.get(spriteId);
			double centeredX = x - sprite.getWidth() / 2;
			double centeredY = y - sprite.getHeight() / 2;
			return create(spriteId, centeredX, centeredY,
					info.getReturnValue(), info.getTimeToLive());
			
		}
		
	}

}
