package net.xuset.triGame.game.ui.arsenal;

import java.util.ArrayList;
import java.util.List;

import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.util.Observer.Change;
import net.xuset.triGame.game.entities.LocManCreator;
import net.xuset.triGame.game.entities.buildings.Building.BuildingInfo;
import net.xuset.triGame.game.guns.GunType;
import net.xuset.triGame.game.shopping.ShopItem;
import net.xuset.triGame.game.shopping.ShopManager;
import net.xuset.triGame.game.shopping.UpgradeManager;
import net.xuset.triGame.game.ui.gameInput.IGunInput;
import net.xuset.triGame.game.ui.upgrades.UiUpgrades;

public class ArsenalForm extends UiForm {
	private final UiButton btnSwitch;
	private final List<ArsenalSubForm> formContainer = new ArrayList<ArsenalSubForm>();
	private int selectedForm = 0;

	public final ArsenalItemAdder itemAdder;
	public final IGunInput arsenalInput;
	
	public ArsenalForm(BuildingAttacher attacher, ShopManager shop, UiUpgrades upgrades) {
		getLayout().setOrientation(Axis.X_AXIS);
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		
		btnSwitch = new UiButton("Guns");
		btnSwitch.addMouseListener(new BtnSwitchClick());
		
		BuildingForm bForm = new BuildingForm(attacher, shop);
		GunForm gForm = new GunForm(shop, upgrades);
		arsenalInput = gForm;
		itemAdder = new ArsenalItemAdder(bForm, gForm);
		addSubForm(bForm);
		addSubForm(gForm);
		
		switchTo(0);
	}
	
	public void addSubForm(ArsenalSubForm subForm) {
		formContainer.add(subForm);
	}
	
	public void onForcedFocusLost() {
		formContainer.get(selectedForm).onForcedFocusLost();
	}
	
	private void switchTo(int formIndex) {
		selectedForm = formIndex;
		getLayout().clearComponents();
		getLayout().add(btnSwitch);
		getLayout().add(formContainer.get(formIndex));
		
		ArsenalSubForm next = formContainer.get(getNextFormIndex());
		btnSwitch.setText(next.name);
		
	}
	
	private int getNextFormIndex() {
		if (selectedForm + 1 >= formContainer.size())
			return 0;
		return selectedForm + 1;
	}
	
	private class BtnSwitchClick implements Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.RELEASE)
				switchTo(getNextFormIndex());
		}
		
	}
	
	public class ArsenalItemAdder {
		private final BuildingForm bForm;
		private final GunForm gForm;
		
		public ArsenalItemAdder(BuildingForm bForm, GunForm gForm) {
			this.bForm = bForm;
			this.gForm = gForm;
		}
		
		public void addBuilding(BuildingInfo info, LocManCreator<?> creator) {
			bForm.addBuilding(info, creator);
		}
		
		public void addGun(GunType type, String name, ShopItem shop,
				UpgradeManager upgrades) {
			gForm.addGun(type, name, shop, upgrades);
		}
		
	}
}
