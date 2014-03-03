package net.xuset.triGame.intro.host;

import java.io.IOException;

import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.IntroForm;

public class IntroHost implements IntroForm {
	private static final String hostGameText = "host game";
	private static final String startGameText = "start game";
	
	private final UiForm frmMain = new UiForm();
	private final UiLabel lblPlayers = new UiLabel("host on port 3000");
	private final UiButton btnStart = new UiButton();
	
	private Network network = null;
	private boolean startGame = false;
	
	public IntroHost() {
		btnStart.setText(hostGameText);
		btnStart.addMouseListener(new BtnStartAction());
		frmMain.setLayout(new UiQueueLayout(5, 20, frmMain));
		frmMain.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		frmMain.getLayout().add(lblPlayers);
		frmMain.getLayout().add(btnStart);
	}

	@Override
	public UiForm getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (startGame)
			return new GameInfo(network, GameType.SURVIVAL, NetworkType.HOST);
		return null;
	}
	
	private class BtnStartAction implements Observer.Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			
			if (btnStart.getText().equals(hostGameText)) {
				try {
					network = Network.startupServer(3000);
					btnStart.setText(startGameText);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				startGame = true;
			}
		}
		
	}

}
