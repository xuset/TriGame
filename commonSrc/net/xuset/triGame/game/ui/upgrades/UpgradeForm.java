package net.xuset.triGame.game.ui.upgrades;

import net.xuset.tSquare.imaging.Sprite;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeItem;
import net.xuset.triGame.game.shopping.UpgradeManager;

public class UpgradeForm extends UiForm {
	private static final int maxPanels = 4;
	
	private final UiLabel lblName = new UiLabel("");
	private final UiForm panelContainer = new UiForm();
	private final UpgradePanel[] panels = new UpgradePanel[maxPanels];
	
	public UpgradeForm(ShopManager shop) {
		getLayout().add(lblName);
		panelContainer.getLayout().setOrientation(Axis.Y_AXIS);
		getLayout().add(panelContainer);
		
		for (int i = 0; i < panels.length; i++)
			panels[i] = new UpgradePanel(shop);
	}
	
	void setUpgrade(Sprite s, UpgradeManager uManager) {
		lblName.setImage(s.createCopy());
		lblName.setText("");
		setUpgradePanels(uManager);
	}
	
	void setUpgrade(String name, UpgradeManager uManager) {
		lblName.setText(name);
		lblName.setImage(null);
		setUpgradePanels(uManager);
	}
	
	private void setUpgradePanels(UpgradeManager uManager) {
		panelContainer.getLayout().clearComponents();
		for (int i = 0; i < uManager.items.size(); i++) {
			UpgradeItem uItem = uManager.items.get(i);
			panels[i].setUpgrade(uItem, uManager);
			panelContainer.getLayout().add(panels[i]);
		}
	}
	
}
