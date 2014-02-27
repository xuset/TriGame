package net.xuset.triGame.game.ui.arsenal;

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

public class ArsenalForm extends UiForm {
	private final UiButton btnSwitch;
	private final ArsenalSubForm[] formContainer;
	private int selectedForm = 0;

	public final ArsenalItemAdder itemAdder;
	
	public ArsenalForm(BuildingAttacher attacher, ShopManager shop) {
		getLayout().setOrientation(Axis.X_AXIS);
		getLayout().setAlignment(Axis.Y_AXIS, Alignment.CENTER);
		
		btnSwitch = new UiButton("Guns");
		btnSwitch.addMouseListener(new BtnSwitchClick());
		
		formContainer = new ArsenalSubForm[2];
		BuildingForm bForm = new BuildingForm(attacher, shop);
		formContainer[0] = bForm;
		GunForm gForm = new GunForm(shop);
		formContainer[1] = gForm;
		itemAdder = new ArsenalItemAdder(bForm, gForm);
		
		switchTo(0);
	}
	
	private void switchTo(int formIndex) {
		selectedForm = formIndex;
		getLayout().clearComponents();
		getLayout().add(btnSwitch);
		getLayout().add(formContainer[formIndex]);
		
		ArsenalSubForm next = formContainer[getNextFormIndex()];
		btnSwitch.setText(next.name);
		
	}
	
	private int getNextFormIndex() {
		if (selectedForm + 1 >= formContainer.length)
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
