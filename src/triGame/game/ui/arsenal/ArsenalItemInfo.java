package triGame.game.ui.arsenal;

import java.awt.image.BufferedImage;

import triGame.game.shopping.ShopItem;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.Attacher;

public class ArsenalItemInfo {
	public Attacher.AttachedItem attachedItem = null;
	public boolean isImagge;
	public BufferedImage image;
	public String text;
	public ShopItem shopItem;
	public ArsenalItem arsenalItem;
	public ArsenalGroup arsenalGroup;
	public UpgradeManager upgradeManager;
	public String description = "-";
}
