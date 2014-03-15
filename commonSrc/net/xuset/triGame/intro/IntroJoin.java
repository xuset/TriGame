package net.xuset.triGame.intro;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.xuset.objectIO.netObject.ObjController;
import net.xuset.objectIO.util.broadcast.BroadcastClient;
import net.xuset.tSquare.imaging.TsColor;
import net.xuset.tSquare.math.IdGenerator;
import net.xuset.tSquare.system.Network;
import net.xuset.tSquare.system.input.mouse.MouseAction;
import net.xuset.tSquare.system.input.mouse.TsMouseEvent;
import net.xuset.tSquare.ui.Alignment;
import net.xuset.tSquare.ui.Axis;
import net.xuset.tSquare.ui.UiButton;
import net.xuset.tSquare.ui.UiComponent;
import net.xuset.tSquare.ui.UiFixedGridList;
import net.xuset.tSquare.ui.UiForm;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.game.GameInfo;
import net.xuset.triGame.game.GameInfo.NetworkType;

class IntroJoin implements IntroForm{
	private static final long scanInterval = 2000L;
	
	private final IntroSwitcher introSwitcher;
	private final UiForm frmMain;
	private final UiLabel lblStatus = new UiLabel("");
	private final UiFixedGridList gridList = new UiFixedGridList(2, 5, 300);
	
	private ArrayList<Host> hostsList = new ArrayList<Host>();
	private Network network = null;
	private NetworkGameStarter gameStarter;
	private BroadcastClient broadcastReciever = null;
	private long lastScanTime = 0L;
	
	public IntroJoin(IntroSwitcher introSwitcher) {
		this.introSwitcher = introSwitcher;
		frmMain = createFrmMain();
		setStatus(false, "Searching for open games...");
	}
	
	private UiForm createFrmMain() {
		UiForm main = new UiForm();
		UiQueueLayout layout = new UiQueueLayout(5, 20, main);
		main.setLayout(layout);
		
		UiForm frmButtons = createFrmButtons();
		UiForm frmGrid = createFrmGrid(frmButtons);
		
		UiButton btnHost = new UiButton("Host new game");
		btnHost.addMouseListener(new BtnHostAction());
		
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		layout.add(lblStatus);
		layout.add(frmGrid);
		layout.add(btnHost);
		
		return main;
	}
	
	private UiForm createFrmButtons() {
		UiForm frmButtons = new UiForm();
		UiButton btnRefresh = new UiButton("Refresh");
		UiButton btnConnect = new UiButton("Connect");
		
		btnConnect.addMouseListener(new BtnConnectAction());
		btnRefresh.addMouseListener(new BtnRefreshAction());

		frmButtons.getLayout().setOrientation(Axis.Y_AXIS);
		frmButtons.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmButtons.getLayout().add(btnRefresh);
		frmButtons.getLayout().add(btnConnect);
		
		return frmButtons;
	}
	
	private UiForm createFrmGrid(UiForm frmButtons) {
		UiForm frmGrid = new UiForm();
		frmGrid.getLayout().setAlignment(Axis.Y_AXIS, Alignment.BACK);
		frmGrid.getLayout().add(gridList);
		frmGrid.getLayout().add(frmButtons);
		return frmGrid;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		if (gameStarter != null && gameStarter.hasGameStarted()) {
			broadcastReciever.stop();
			return new GameInfo(network, gameStarter.getGameType(), NetworkType.JOIN);
		}
		return null;
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public void onFocusGained() {
		createBroadcast();
	}

	@Override
	public void onFocusLost() {
		disconnectNetwork();
		gameStarter = null;
		destroyBroadcast();
	}

	@Override
	public void update() {
		if (lastScanTime + scanInterval < System.currentTimeMillis())
			pollBroadcast();
	}
	
	private void pollBroadcast() {
		gridList.clearRows();
		hostsList.clear();
		
		String recieved = null;
		while ((recieved = broadcastReciever.pollMsgQueue()) != null) {
			try {
				
				BroadcastMsg msg = new BroadcastMsg(recieved);
				Host host = new Host(msg.getHost(), msg.getPort());
				
				hostsList.add(host);
				gridList.addRow("" + hostsList.size(), host.toString());
				
			}
			catch (IllegalArgumentException ex) { }
			catch (UnknownHostException ex) { }
		}

		lastScanTime = System.currentTimeMillis();
	}
	
	private void createBroadcast() {
		destroyBroadcast();
		
		int port = IntroHost.MULTICAST_PORT;
		String host = IntroHost.MULTICAST_GROUP;
		try {
			broadcastReciever = new BroadcastClient(port, host);
		} catch (IOException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void destroyBroadcast() {
		if (broadcastReciever != null)
			broadcastReciever.stop();
		broadcastReciever = null;
	}
	
	private void disconnectNetwork() {
		if (network != null)
			network.disconnect();
		network = null;
	}
	
	private void tryConnecting(String addr, int port) {
		try {
			disconnectNetwork();
			network = Network.connectToServer(addr, port, IdGenerator.getNext());
			gameStarter = new NetworkGameStarter(new ObjController(network.hub));
			lblStatus.setText("Connected. Waiting on host to start.");
		} catch (IOException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void setStatus(boolean isError, String status) {
		lblStatus.setForeground(isError ? TsColor.red : TsColor.black);
		lblStatus.setText(status);
	}
	
	private class BtnConnectAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			
			int selected = gridList.getSelectedRowIndex();
			if (selected < hostsList.size()) {
				setStatus(false, "Attempting to connect...");
				Host host = hostsList.get(selected);
				tryConnecting(host.getAddress(), host.getPort());
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
	
	private class BtnRefreshAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;

			setStatus(false, "Searching for open games...");
			pollBroadcast();
		}
	}
	
	private class Host {
		private final String addr;
		private final int port;
		private final InetAddress iNetAddr;
		
		public String getAddress() { return addr; }
		public int getPort() { return port; }
		
		public Host(String addr, int port) throws UnknownHostException {
			this.addr = addr;
			this.port = port;
			iNetAddr = InetAddress.getByName(addr);
		}
		
		@Override
		public String toString() { return iNetAddr.getHostName(); }
	}

}
