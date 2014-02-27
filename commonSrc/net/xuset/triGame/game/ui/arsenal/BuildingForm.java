package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;
import net.xuset.triGame.game.shopping.ShopManager;

public class BuildingForm extends ArsenalSubForm {
	private final BuildingAttacher attacher;
	private final ShopManager shop;
	
	public BuildingForm(BuildingAttacher attacher, ShopManager shop) {
		super("Towers");
		this.attacher = attacher;
		this.shop = shop;
	}
	
	public void addBuilding(BuildingInfo info, LocManCreator<?> creator) {
		IImage img = Sprite.get(info.spriteId).createCopy();
		BuildingPanel bp = new BuildingPanel(info.item, creator, img, attacher, shop);
		getLayout().add(bp);
	}
}
