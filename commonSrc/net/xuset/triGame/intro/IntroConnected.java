package net.xuset.triGame.intro;

import net.xuset.objectIO.connections.sockets.ServerEventListener;
import net.xuset.objectIO.connections.sockets.groupNet.client.GroupClientCon;
import net.xuset.objectIO.netObject.StandardObjUpdater;
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
	private static final String playerText = "Players joined: ";
	
	private final UiForm frmMain = new UiForm();
	private final UiLabel lblPlayers = new UiLabel();
	private final ConnectionListener listener = new ConnectionListener();
	
	private Network network = null;
	private NetworkGameStarter gameStarter = null;
	
	public IntroConnected() {
		UiLabel lblConnected = new UiLabel();
		lblConnected.setText("Connected. Waiting on host to start.");
		frmMain.setLayout(new UiQueueLayout(20, 20, frmMain));
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		frmMain.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmMain.getLayout().add(lblConnected);
		frmMain.getLayout().add(lblPlayers);
	}
	
	public void setNetwork(Network network) {
		disconnectNetwork();
		this.network = network;
		network.getClientInstance().watchEvents(listener);
		gameStarter = new NetworkGameStarter(new StandardObjUpdater(network.hub));
		updatePlayerCount();
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (gameStarter != null && gameStarter.hasGameStarted()) {
			return new GameInfo(network, gameStarter.getGameType(), NetworkType.JOIN);
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
		lblPlayers.setText(playerText + network.hub.getConnectionCount());
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
