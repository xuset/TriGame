package net.xuset.triGame.game.ui.arsenal;

import java.util.ArrayList;

import net.xuset.tSquare.imaging.IImage;
import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;
import net.xuset.triGame.game.shopping.ShopManager;

public class BuildingForm extends ArsenalSubForm {
	private final BuildingAttacher attacher;
	private final ShopManager shop;
	private final ArrayList<BuildingPanel> panels = new ArrayList<BuildingPanel>();
	private final UiTrashCan trashCan;
	
	public BuildingForm(BuildingAttacher attacher, ShopManager shop) {
		super("Towers");
		this.attacher = attacher;
		this.shop = shop;
		trashCan = new UiTrashCan(attacher);
	}
	
	public void addBuilding(BuildingInfo info, LocManCreator<?> creator) {
		IImage img = Sprite.get(info.spriteId).image;
		BuildingPanel bp = new BuildingPanel(info.item, creator, img, attacher, shop,
				info.visibilityRadius);
		panels.add(bp);
		refreshView();
	}
	
	private void refreshView() {
		getLayout().clearComponents();
		for (BuildingPanel bp : panels) {
			getLayout().add(bp);
		}
		getLayout().add(trashCan);
	}
}
