package net.xuset.triGame.intro;

import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;

class UtilityForm extends UiForm {
	private final IntroSwitcher switcher;
	private final UiForm frmButtons;
	private final UiButton btnBack = new UiButton("<- back");
	private final UiButton btnSettings = new UiButton("Settings");
	private final UiLabel lblUpdates = new UiLabel(""); //for future use
	
	public UtilityForm(IntroSwitcher switcher) {
		this.switcher = switcher;
		
		frmButtons = createFrmButtons();
		finishSetup();
	}
	
	private void finishSetup() {
		getLayout().setOrientation(Axis.Y_AXIS);
		getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		
		getLayout().add(frmButtons);
		getLayout().add(new UiLabel("Created by Austin Middleton. http://xuset.net"));
		getLayout().add(new UiLabel("Licensed under the GPLv2."));
		getLayout().add(lblUpdates);
		btnBack.addMouseListener(new BtnBackAction());
		btnSettings.addMouseListener(new BtnSettingsAction());
		
	}
	
	private UiForm createFrmButtons() {
		UiForm form = new UiForm();
		UiQueueLayout layout = new UiQueueLayout(10, 5, form);
		form.setLayout(layout);
		
		layout.add(btnBack);
		layout.add(btnSettings);
		return form;
	}
	
	public void setButtonOptions(boolean showBack, boolean showSettings) {
		frmButtons.getLayout().clearComponents();
		if (showBack)
			frmButtons.getLayout().add(btnBack);
		if (showSettings)
			frmButtons.getLayout().add(btnSettings);
	}
	
	private class BtnBackAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS)
				switcher.switchToForm(GameIntroForms.MAIN);
		}
	}
	
	private class BtnSettingsAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS)
				switcher.switchToForm(GameIntroForms.SETTINGS);
		}
	}
}
