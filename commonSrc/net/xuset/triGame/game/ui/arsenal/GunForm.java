package net.xuset.triGame.game.ui.arsenal;

import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.layout.UiLayout;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeManager;
import net.xuset.triGame.game.ui.gameInput.IGunInput;
import net.xuset.triGame.game.ui.upgrades.UiUpgrades;

public class GunForm extends ArsenalSubForm implements IGunInput{
	private final ShopManager shop;
	private final UiUpgrades uiUpgrades;
	private GunType selectedGun = GunType.PISTOL;
	private boolean requestGunChange = false;
	
	public GunForm(ShopManager shop, UiUpgrades uiUpgrades) {
		super("Guns");
		this.shop = shop;
		this.uiUpgrades = uiUpgrades;
		getLayout().setOrientation(Axis.X_AXIS);
	}
	
	public void addGun(GunType type, String name, ShopItem item, UpgradeManager upgrades) {
		GunPanel gp = new GunPanel(type, name, item, upgrades, shop, uiUpgrades);
		getLayout().add(gp);
		
		if (getLayout().getComponentCount() == 1)
			gp.setSelected(true);
	}

	@Override
	protected void recieveMouseEvent(TsMouseEvent e, float x, float y) {
		super.recieveMouseEvent(e, x, y);
		if (e.action != MouseAction.PRESS)
			return;
		
		UiLayout l = getLayout();
		
		GunPanel selected = null;
		for (int i = 0; i < l.getComponentCount(); i++) {
			GunPanel gp = (GunPanel) l.getComponent(i);
			if (gp.contains(x, y) && gp.hasPurchased())
				selected = gp;
		}
		
		if (selected != null)
			setSelectedGunPanel(selected);
	}
	
	private void setSelectedGunPanel(GunPanel selected) {
		requestGunChange = true;
		if (selectedGun == selected.getGunType())
			uiUpgrades.showUpgrades(selected.getGunName(), selected.getGunUpgradeManager());
		selectedGun = selected.getGunType();
		for (int i = 0; i < getLayout().getComponentCount(); i++) {
			GunPanel gp = (GunPanel) getLayout().getComponent(i);
			
			gp.setSelected(gp == selected);
		}
	}

	@Override
	public boolean shootRequested() {
		return false;
	}

	@Override
	public boolean changeGunRequested() {
		if (requestGunChange) {
			requestGunChange = false;
			return true;
		}
		return false;
	}

	@Override
	public GunType getCurrentGunType() {
		return selectedGun;
	}
}
