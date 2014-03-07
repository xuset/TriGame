package net.xuset.triGame.intro.host;

import java.io.IOException;

import net.xuset.objectIO.connections.sockets.p2pServer.server.ConnectionEvent;
import net.xuset.objectIO.connections.sockets.p2pServer.server.P2PServer;
import net.xuset.objectIO.connections.sockets.p2pServer.server.ServerConnection;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.IntroForm;

public class IntroHost implements IntroForm {
	private static final String joinedText = "Players joined: ";
	
	private final UiForm frmMain = new UiForm();
	private final UiLabel lblStatus = new UiLabel("Waiting for players to join");
	
	private final UiButton btnStart = new UiButton("Start game");

	private Network network;
	private boolean startGame = false;
	
	public IntroHost() {
		btnStart.addMouseListener(new BtnStartAction());
		
		frmMain.setLayout(new UiQueueLayout(5, 20, frmMain));
		frmMain.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		frmMain.getLayout().add(lblStatus);
		frmMain.getLayout().add(btnStart);
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (startGame)
			return new GameInfo(network, GameType.SURVIVAL, NetworkType.HOST);
		return null;
	}

	@Override
	public void onFocusGained() {
		try {
			network = Network.startupServer(3000);
			network.getServerInstance().event = new ServerConnectionEvent();
		} catch (IOException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void onFocusLost() {
		if (network != null)
			network.disconnect();
		network = null;
	}

	@Override
	public void update() {
		
	}
	
	private void setStatus(boolean isError, String status) {
		lblStatus.setForeground(isError ? TsColor.red : TsColor.black);
		lblStatus.setText(status);
	}
	
	private class BtnStartAction implements Observer.Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS)
				startGame = true;
		}
	}
	
	private class ServerConnectionEvent implements ConnectionEvent {
		@Override
		public void onConnect(P2PServer s, ServerConnection c) {
			setStatus(false, joinedText + s.connections.size());
		}

		@Override
		public void onDisconnect(P2PServer s, ServerConnection c) {
			setStatus(false, joinedText + s.connections.size());
		}

		@Override
		public void onLastDisconnect(P2PServer s) {
			
		}
	}

}
