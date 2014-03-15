package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;
import net.xuset.triGame.game.shopping.ShopManager;

public class BuildingForm extends ItemForm {
	private final BuildingAttacher attacher;
	private final ShopManager shop;
	private final UiTrashCan trashCan;
	
	public BuildingForm(BuildingAttacher attacher, ShopManager shop) {
		super("Towers");
		this.attacher = attacher;
		this.shop = shop;
		trashCan = new UiTrashCan(attacher);
		addItem(trashCan);
	}
	
	public void addBuilding(BuildingInfo info, LocManCreator<?> creator) {
		IImage img = Sprite.get(info.spriteId).image;
		BuildingPanel bp = new BuildingPanel(info.item, creator, img, attacher, shop,
				info.visibilityRadius, (ItemMetaSetter) this, info.description);
		addItem(bp);
	}
}
