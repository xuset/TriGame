package net.xuset.triGame.intro.join;

import java.io.IOException;
import java.net.UnknownHostException;

import net.xuset.tSquare.math.IdGenerator;
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
import net.xuset.triGame.intro.GameIntroForms;
import net.xuset.triGame.intro.IntroForm;
import net.xuset.triGame.intro.IntroSwitcher;

public class IntroJoin implements IntroForm {
	private static final String ip = "127.0.0.1";
	private static final int port = 3000;
	
	private final IntroSwitcher introSwitcher;
	private final UiForm mainForm = new UiForm();
	private final UiLabel lblInfo = new UiLabel("connect to " + ip + ":" + port);
	private final UiButton btnConnect = new UiButton("Connect");
	private final UiButton btnHost = new UiButton("Host new game");
	private Network network = null;
	
	public IntroJoin(IntroSwitcher introSwitcher) {
		this.introSwitcher = introSwitcher;
		UiQueueLayout layout = new UiQueueLayout(5, 20, mainForm);
		mainForm.setLayout(layout);
		
		btnConnect.addMouseListener(new BtnConnectAction());
		btnHost.addMouseListener(new BtnHostAction());
		
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		layout.add(lblInfo);
		layout.add(btnConnect);
		layout.add(btnHost);
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (network == null)
			return null;
		else
			return new GameInfo(network, GameType.SURVIVAL, NetworkType.JOIN);
	}

	@Override
	public UiForm getForm() {
		return mainForm;
	}
	
	private class BtnConnectAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			
			try {
				network = Network.connectToServer(ip, port, IdGenerator.getNext());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class BtnHostAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			introSwitcher.switchToForm(GameIntroForms.HOST);
		}
	}

}
