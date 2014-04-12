package net.xuset.triGame.intro;

import net.xuset.objectIO.connections.sockets.ServerEventListener;
import net.xuset.objectIO.connections.sockets.groupNet.client.GroupClientCon;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;

public class IntroConnected implements IntroForm{
	private static final String connectedText = "Connected. Waiting on host to start.";
	private static final String errorText = "Could not connect to host.";
	private static final String playerText = "Players joined: ";
	
	private final UiForm frmMain = new UiForm();
	private final UiLabel lblPlayers = new UiLabel();
	private final UiLabel lblStatus = new UiLabel(connectedText);
	private final ConnectionListener listener = new ConnectionListener();
	
	private Network network = null;
	private NetworkGameStarter gameStarter = null;
	private boolean createOnNext = false;
	
	public IntroConnected() {
		frmMain.setLayout(new UiQueueLayout(20, 20, frmMain));
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		frmMain.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmMain.getLayout().add(lblStatus);
		frmMain.getLayout().add(lblPlayers);
	}
	
	public void setNetwork(Network network) {
		setStatus(false, connectedText);
		disconnectNetwork();
		this.network = network;
		network.getClientInstance().watchEvents(listener);
		gameStarter = new NetworkGameStarter(network.hub, network.objController);
		updatePlayerCount();
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (gameStarter != null && gameStarter.hasGameStarted()) {
			if (createOnNext)
				return new GameInfo(network, gameStarter.getGameType(), NetworkType.JOIN);
			else {
				createOnNext = true;
				setStatus(false, "Loading game...");
			}
		}
		return null;
	}

	@Override
	public void onFocusGained() {
		
	}

	@Override
	public void onFocusLost() {
		disconnectNetwork();
	}

	@Override
	public void update() {
		if (!network.getClientInstance().isConnectedToServer() &&
				!lblStatus.getText().equals(errorText)) {
			
			setStatus(true, errorText);
			lblPlayers.setText("");
		}
	}
	
	private void setStatus(boolean error, String text) {
		lblStatus.setText(text);
		lblStatus.setForeground(error ? TsColor.red : TsColor.black);
	}
	
	private void disconnectNetwork() {
		if (network != null) {
			network.getClientInstance().unwatchEvents(listener);
			network.disconnect();
		}
		network = null;
		gameStarter = null;
	}
	
	private void updatePlayerCount() {
		if (network.getClientInstance().isConnectedToServer())
			lblPlayers.setText(playerText + (network.hub.getConnectionCount()));
		else
			lblPlayers.setText("");
	}
	
	private class ConnectionListener implements ServerEventListener<GroupClientCon> {

		@Override
		public void onRemove(GroupClientCon con) {
			updatePlayerCount();
		}

		@Override
		public void onAdd(GroupClientCon con) {
			updatePlayerCount();
		}

		@Override
		public void onLastRemove() {
			
		}

		@Override
		public void onShutdown() {
			
		}
	}

}
