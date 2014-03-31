package net.xuset.triGame.intro;

import java.io.IOException;
import java.net.InetAddress;

import net.xuset.objectIO.connections.sockets.ServerEventListener;
import net.xuset.objectIO.connections.sockets.groupNet.server.GroupServerCon;
import net.xuset.objectIO.netObject.StandardObjUpdater;
import net.xuset.objectIO.util.broadcast.BroadcastServer;
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
import net.xuset.tSquare.ui.UiRadioGroup;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;
import net.xuset.triGame.game.GameMode.GameType;


class IntroHost implements IntroForm {
	public static final String MULTICAST_GROUP = "230.0.0.1";
	public static final int MULTICAST_PORT = 4000;
	private static final String joinedText = "Players joined: ";
	private static final String survivalText = "Survival", versusText = "Versus";
	
	private final UiForm frmMain = new UiForm();
	private final UiLabel lblStatus = new UiLabel("Waiting for players to join");
	private final UiLabel lblPlayers = new UiLabel(joinedText + 0);
	private final UiRadioGroup radioGroup = new UiRadioGroup();
	private final IpGetterIFace ipGetter;

	private BroadcastServer broadcaster;
	private NetworkGameStarter gameStarter;
	private Network network;
	
	public IntroHost(IpGetterIFace ipGetter) {
		this.ipGetter = ipGetter;
		UiButton btnStart = new UiButton("Start game");
		btnStart.addMouseListener(new BtnStartAction());
		
		radioGroup.addButton(survivalText);
		radioGroup.addButton(versusText);
		
		frmMain.setLayout(new UiQueueLayout(5, 20, frmMain));
		frmMain.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmMain.getLayout().setOrientation(Axis.Y_AXIS);
		frmMain.getLayout().add(lblStatus);
		frmMain.getLayout().add(radioGroup);
		frmMain.getLayout().add(lblPlayers);
		frmMain.getLayout().add(btnStart);
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (gameStarter.hasGameStarted()) {
			network.getServerInstance().getAcceptor().stop();
			broadcaster.stop();
			broadcaster = null;
			return new GameInfo(network, gameStarter.getGameType(), NetworkType.HOST);
		}
		return null;
	}

	@Override
	public void onFocusGained() {
		createServer();
	}

	@Override
	public void onFocusLost() {
		destroyServer();
	}

	@Override
	public void update() {
		
	}
	
	private void createServer() {
		try {
			
			network = Network.startupServer(0);
			network.getServerInstance().watchEvents(new ServerConnectionEvent());
			gameStarter = new NetworkGameStarter(new StandardObjUpdater(network.hub));
			int port = network.getServerInstance().getPort();
			InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
			InetAddress local = ipGetter.getLocalIP();
			if (local == null)
				throw new IOException("Cannot determine local IP address.");
			broadcaster = new BroadcastServer(MULTICAST_PORT, group);
			BroadcastMsg bMsg = new BroadcastMsg(local, port);
			broadcaster.start(bMsg.toString());
			
		} catch (IOException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
			destroyServer();
		}
	}
	
	private void destroyServer() {
		if (broadcaster != null)
			broadcaster.stop();
		if (network != null)
			network.disconnect();
		
		gameStarter = null;
		broadcaster = null;
		network = null;
	}
	
	private void setStatus(boolean isError, String status) {
		lblStatus.setForeground(isError ? TsColor.red : TsColor.black);
		lblStatus.setText(status);
	}
	
	private class BtnStartAction implements Observer.Change<TsMouseEvent> {

		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action == MouseAction.PRESS) {
				GameType type =
						radioGroup.getSelectedButton().getText().equals(versusText) ?
						GameType.VERSUS : GameType.SURVIVAL;
				gameStarter.startGame(type);
				network.flush();
			}
		}
	}
	
	private class ServerConnectionEvent implements ServerEventListener<GroupServerCon> {

		@Override
		public void onRemove(GroupServerCon con) {
			update();
		}

		@Override
		public void onAdd(GroupServerCon con) {
			update();
		}
		
		private void update() {
			int count = 0;
			if (network != null)
				count = network.getServerInstance().getConnectionCount() - 1;
			lblPlayers.setText(joinedText + count);
		}

		@Override
		public void onLastRemove() {
			//Do nothing
		}

		@Override
		public void onShutdown() {
			//Do nothing
			
		}
	}

}
