package net.xuset.triGame.intro.main;

import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.intro.GameIntroForms;
import net.xuset.triGame.intro.IntroForm;
import net.xuset.triGame.intro.IntroSwitcher;

public class IntroMain implements IntroForm {
	
	private final IntroSwitcher introSwticher;
	private final UiForm frmMain = new UiForm();
	private final UiButton btnSolo, btnMulti;
	
	public IntroMain(IntroSwitcher introSwitcher) {
		this.introSwticher = introSwitcher;
		UiQueueLayout mainLayout = new UiQueueLayout(20, 5, frmMain);
		frmMain.setLayout(mainLayout);
		
		btnSolo = new UiButton("Solo");
		btnMulti = new UiButton("Multiplayer");
		
		btnSolo.addMouseListener(new BtnSoloAction());
		btnMulti.addMouseListener(new BtnMultiAction());
		
		mainLayout.add(btnSolo);
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
