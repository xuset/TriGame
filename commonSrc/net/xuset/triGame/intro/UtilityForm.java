package net.xuset.triGame.intro;

import net.xuset.tSquare.imaging.IGraphics;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.Params;
import net.xuset.triGame.game.ui.IBrowserOpener;

class UtilityForm extends UiForm {
	private final IntroSwitcher switcher;
	private final UpdateChecker updateChecker;
	private final IBrowserOpener browserOpener;
	private final UiForm frmButtons;
	private final UiButton btnBack = new UiButton("<- back");
	private final UiButton btnLeaderboard = new UiButton("Leaderboards");
	private final UiButton btnSettings = new UiButton("Settings");
	private final UiLabel lblUpdates = new UiLabel("");
	
	public UtilityForm(IntroSwitcher switcher, UpdateChecker updateChecker,
			IBrowserOpener browserOpener) {
		
		this.switcher = switcher;
		this.updateChecker = updateChecker;
		this.browserOpener = browserOpener;
		
		frmButtons = createFrmButtons();
		finishSetup();
	}
	
	public void setButtonOptions(boolean showBack, boolean showSettings) {
		frmButtons.getLayout().clearComponents();
		if (showBack)
			frmButtons.getLayout().add(btnBack);
		if (showSettings) {
			frmButtons.getLayout().add(btnSettings);
			frmButtons.getLayout().add(btnLeaderboard);
		}
	}
	
	@Override
	public void draw(IGraphics g) {
		if (lblUpdates.getText().equals("")) {
			setUpdateLabel();
		}
		super.draw(g);
	}
	
	private void setUpdateLabel() {
		if (updateChecker.isFinished()) {
			if (updateChecker.updateAvailable()) {
				lblUpdates.setForeground(new TsColor(200, 20, 20));
				lblUpdates.setText("A new version is available!  Visit  "
						+ updateChecker.getUpdateURL());
			} else {
				lblUpdates.setText("You have the most recent version v" + Params.VERSION);
			}
		}
	}
	
	private void finishSetup() {
		getLayout().setOrientation(Axis.Y_AXIS);
		getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		
		getLayout().add(frmButtons);
		getLayout().add(new UiLabel("Create by Austin Middleton | http://xuset.net"));
		getLayout().add(lblUpdates);
		btnBack.addMouseListener(new BtnBackAction());
		btnSettings.addMouseListener(new BtnSettingsAction());
		btnLeaderboard.addMouseListener(new BtnLeaderboardAction());
		
	}
	
	private UiForm createFrmButtons() {
		UiForm form = new UiForm();
		UiQueueLayout layout = new UiQueueLayout(10, 5, form);
		form.setLayout(layout);
		
		layout.add(btnBack);
		layout.add(btnSettings);
		return form;
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
	
	private class BtnLeaderboardAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS)
				browserOpener.openBrowser("http://xuset.net/polyDefense/scores");
		}
	}
}
