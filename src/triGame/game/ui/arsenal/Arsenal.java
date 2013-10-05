package triGame.game.ui.arsenal;

import javax.swing.JPanel;

import tSquare.imaging.Sprite;
import triGame.game.entities.building.SmallTower;
import triGame.game.entities.building.Tower;
import triGame.game.entities.wall.Barrier;
import triGame.game.entities.wall.TrapDoor;
import triGame.game.projectile.gun.AbstractGun;
import triGame.game.projectile.gun.GunManager;
import triGame.game.shopping.ShopManager;
import triGame.game.ui.Attacher;
import triGame.game.ui.JPanelGetter;
import triGame.game.ui.UserInterface;

public class Arsenal implements JPanelGetter{
	public ArsenalPanel panel;
	public ArsenalGroup gunGroup;
	public ArsenalGroup towerGroup;
	
	private ShopManager shop;
	private Attacher attacher;
	private UserInterface ui;
	
	public JPanel getJPanel() { return panel.panel; }
	
	public Arsenal(UserInterface ui, ShopManager shop, Attacher attacher, GunManager gunManager) {
		this.ui = ui;
		this.shop = shop;
		this.attacher = attacher;
		panel = new ArsenalPanel(shop);
		
		ArsenalGroup.PurchaseEvent gunEvent = new ArsenalGroup.PurchaseEvent() {
			@Override
			public void purchase(ArsenalItemInfo info) {
				if (Arsenal.this.shop.hasPurchased(info.shopItem)) {
					Arsenal.this.ui.switchTo(Arsenal.this.ui.upgrades);
					Arsenal.this.ui.upgrades.set(info.upgradeManager, info.text);
				} else if (Arsenal.this.shop.purchase(info.shopItem)){
					info.arsenalItem.lblPrice.setText("");
				}
			}
		};
		ArsenalGroup.PurchaseEvent towerEvent = new ArsenalGroup.PurchaseEvent() {
			@Override
			public void purchase(ArsenalItemInfo info) {
				if (Arsenal.this.attacher.shopItem == info.shopItem) {
					Arsenal.this.attacher.setAttached(info.shopItem, info.image, false, info.radius);
				} else {
					Arsenal.this.attacher.setAttached(info.shopItem, info.image, true, info.radius);
				}
			}
		};
		
		gunGroup = new ArsenalGroup("Guns", gunEvent);
		towerGroup = new ArsenalGroup("Towers", towerEvent);
		
		ArsenalItem barrier = new ArsenalItem(Barrier.NEW_BARRIER, Sprite.get(Barrier.SPRITE_ID).getBuffered());
		ArsenalItem trapDoor = new ArsenalItem(TrapDoor.NEW_TRAP_DOOR, Sprite.get(TrapDoor.SPRITE_ID).getBuffered());
		ArsenalItem tower = new ArsenalItem(Tower.NEW_TOWER, Sprite.get(Tower.SPRITE_ID).getBuffered(), Tower.INITIAL_RANGE);
		ArsenalItem sTower = new ArsenalItem(SmallTower.NEW_TOWER, Sprite.get(SmallTower.SPRITE_ID).getBuffered(), SmallTower.INITIAL_RANGE);
		
		for (AbstractGun gun : gunManager.getShopableGuns()) {
			ArsenalItem gunItem = new ArsenalItem(gun.getUnlockItem(), gun.name, gun.getUpgradeManager());
			panel.addToArsenal(gunGroup, gunItem);
		}
		
		panel.addToArsenal(towerGroup, barrier);
		panel.addToArsenal(towerGroup, trapDoor);
		panel.addToArsenal(towerGroup, tower);
		panel.addToArsenal(towerGroup, sTower);
		panel.switchGroup(towerGroup);
	}
}
