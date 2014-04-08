package net.xuset.triGame.game.entities.buildings;

import net.xuset.tSquare.game.entity.EntityKey;
import net.xuset.tSquare.imaging.AnimatedSprite;
import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.math.rect.IRectangleR;
import net.xuset.triGame.game.shopping.ShopItem;


public abstract class Wall extends Building {
	public static final String CRACKS_SPRITE_ID = "media/WallCracks.png";
	private final AnimatedSprite cracks = AnimatedSprite.GET(CRACKS_SPRITE_ID);

	public Wall(String spriteId, double x, double y, BuildingInfo info,
			EntityKey key) {
		super(spriteId, x, y, null, info, key);
	}
	
	public static class WallInfo extends BuildingInfo {
		
		public WallInfo(String spriteId, String identifier,
				String description, ShopItem item, int maxHealth) {
			
			super(spriteId, identifier, 0, description, item, false, false, false, 10, maxHealth);
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		IRectangleR rect = g.getView();
		if (!visible || !isOnScreen(rect))
			return;
		
		super.draw(g);
		
		int index = (int) Math.ceil((cracks.total) * (1.0 - getHealth() / info.maxHealth));
		index -= 1;
		
		if (index >= 0 && index < cracks.total) {
			Sprite s = cracks.sprites[index];
			s.draw((float) getX(), (float) getY(), g);
		}
	}

}
