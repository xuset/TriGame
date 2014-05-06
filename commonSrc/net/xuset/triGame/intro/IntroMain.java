package net.xuset.triGame.intro;

import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.Params;
import net.xuset.triGame.game.GameInfo;

class IntroMain implements IntroForm {
	
	private final IntroSwitcher introSwticher;
	private final UiForm frmMain = new UiForm();
	private final UiButton btnSolo, btnMulti;
	
	public IntroMain(IntroSwitcher introSwitcher) {
		this.introSwticher = introSwitcher;
		UiQueueLayout mainLayout = new UiQueueLayout(20, 5, frmMain);
		frmMain.setLayout(mainLayout);
		
		btnSolo = new UiButton(Params.ENABLE_MULTIPLAYER ? "Solo" : "Start game");
		btnMulti = new UiButton("Multiplayer");
		
		btnSolo.addMouseListener(new BtnSoloAction());
		btnMulti.addMouseListener(new BtnMultiAction());
		
		mainLayout.add(btnSolo);
		if (Params.ENABLE_MULTIPLAYER)
			mainLayout.add(btnMulti);
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		return null;
	}

	@Override
	public void onFocusGained() {
		//Do nothing
	}

	@Override
	public void onFocusLost() {
		//Do nothing
	}

	@Override
	public void update() {
		//Do nothing
	}
	
	private class BtnSoloAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS) {
				introSwticher.switchToForm(GameIntroForms.SOLO);
			}
		}
	}
	
	private class BtnMultiAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS) {
				introSwticher.switchToForm(GameIntroForms.JOIN);
			}
		}
	}

}
