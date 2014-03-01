package net.xuset.triGame.game.ui.upgrades;

import net.xuset.tSquare.imaging.Sprite;
import net.xuset.triGame.game.shopping.UpgradeManager;
import net.xuset.triGame.game.ui.UiFormSwitcher;
import net.xuset.triGame.game.ui.UiFormTypes;

public class UiUpgrades {
	private final UpgradeForm upgradeForm;
	private final UiFormSwitcher uiSwitcher;
	
	public UiUpgrades(UpgradeForm upgradeForm, UiFormSwitcher uiSwitcher) {
		this.upgradeForm = upgradeForm;
		this.uiSwitcher = uiSwitcher;
	}
	
	public void showUpgrades(Sprite image, UpgradeManager um) {
		upgradeForm.setUpgrade(image, um);
		uiSwitcher.switchView(UiFormTypes.UPGRADES);
	}
	
	public void showUpgrades(String name, UpgradeManager um) {
		upgradeForm.setUpgrade(name, um);
		uiSwitcher.switchView(UiFormTypes.UPGRADES);
	}
}
