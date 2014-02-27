package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeManager;

public class GunForm extends ArsenalSubForm {
	private final ShopManager shop;
	
	public GunForm(ShopManager shop) {
		super("Guns");
		this.shop = shop;
		getLayout().setOrientation(Axis.X_AXIS);
	}
	
	public void addGun(GunType type, String name, ShopItem item, UpgradeManager upgrades) {
		GunPanel gp = new GunPanel(type, name, item, upgrades, shop);
		getLayout().add(gp);
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		UiLayout l = getLayout();
		
		GunPanel selected = null;
		for (int i = 0; i < l.getComponentCount(); i++) {
			GunPanel gp = (GunPanel) l.getComponent(i);
			if (gp.contains(x, y))
				selected = gp;
		}
		
		if (selected != null) {
			setSelectedGunPanel(selected);
			//TODO somehow implement a IGunInput
		}
	}
	
	private void setSelectedGunPanel(GunPanel selected) {
		for (int i = 0; i < getLayout().getComponentCount(); i++) {
			GunPanel gp = (GunPanel) getLayout().getComponent(i);
			
			gp.setSelected(gp == selected);
		}
	}
}
