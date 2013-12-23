package triGame.game.ui.arsenal;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import tSquare.imaging.Sprite;
import triGame.game.entities.LocManCreator;
import triGame.game.entities.buildings.Building;
import triGame.game.shopping.ShopItem;
import triGame.game.shopping.ShopManager;
import triGame.game.shopping.UpgradeManager;
import triGame.game.ui.Attacher;
import triGame.game.ui.JPanelGetter;
import triGame.game.ui.UserInterface;

public class Arsenal implements JPanelGetter{
	public final ArsenalPanel panel;
	public final ArsenalGroup gunGroup;
	public final ArsenalGroup towerGroup;
	
	private final ShopManager shop;
	private final Attacher attacher;
	private final UserInterface ui;
	
	public JPanel getJPanel() { return panel.pnlSplit; }
	
	public Arsenal(UserInterface ui, ShopManager shop, Attacher attacher) {
		this.ui = ui;
		this.shop = shop;
		this.attacher = attacher;
		panel = new ArsenalPanel(shop, ui.focus);
		
		ArsenalGroup.PurchaseEvent gunEvent = new ArsenalGroup.PurchaseEvent() {
			@Override
			public void purchase(ArsenalItemInfo info) {
				if (Arsenal.this.shop.hasPurchased(info.shopItem)) {
					Arsenal.this.ui.switchTo(Arsenal.this.ui.upgrades);
					Arsenal.this.ui.upgrades.set(info.upgradeManager, info.text);
				} else if (Arsenal.this.shop.purchase(info.shopItem)){
					info.arsenalItem.removePrice();
					info.description = "Click to choose upgrades";
				}
			}
		};
		ArsenalGroup.PurchaseEvent towerEvent = new ArsenalGroup.PurchaseEvent() {
			@Override
			public void purchase(ArsenalItemInfo info) {
				Attacher attacher = Arsenal.this.attacher;
				if (attacher.isAttached() &&
						attacher.getAttached().shopItem == info.shopItem) {
					
					attacher.setAttached(info.attachedItem, false);
				} else {
					attacher.setAttached(info.attachedItem, true);
				}
			}
		};
		
		gunGroup = new ArsenalGroup("Guns", gunEvent);
		towerGroup = new ArsenalGroup("Towers", towerEvent);
		
		panel.groups.add(gunGroup);
		panel.groups.add(towerGroup);
	}
	
	public void addTower(Building.BuildingInfo info, LocManCreator<?> creator) {
		addToArsenal(info.item, Sprite.get(info.spriteId).createCopy(), info.description, info.visibilityRadius, towerGroup, creator);
	}
	
	public void addGun(ShopItem item, String name, String description, UpgradeManager upgrades) {
		panel.addArsenalItem(gunGroup, new ArsenalItem(item, name, description, upgrades, panel));
	}
	
	public void addToArsenal(ShopItem item, BufferedImage image, String description,
			int radius, ArsenalGroup group, LocManCreator<?> creator) {
		
		panel.addArsenalItem(group, new ArsenalItem(item, image, radius, creator, description, panel));
	}
}
