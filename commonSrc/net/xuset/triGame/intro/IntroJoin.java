package net.xuset.triGame.intro;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import net.xuset.tSquare.ui.UiGridList;
import net.xuset.tSquare.ui.UiLabel;
import net.xuset.tSquare.ui.layout.UiQueueLayout;
import net.xuset.tSquare.util.Observer;
import net.xuset.triGame.Params;
import net.xuset.triGame.Version;
import net.xuset.triGame.game.GameInfo;

class IntroJoin implements IntroForm{
	private static final long scanInterval = 2000L;
	
	private final IntroSwitcher introSwitcher;
	private final IntroConnected introConnected;
	private final UiForm frmMain;
	private final UiLabel lblStatus = new UiLabel("");
	private final UiFixedGridList gridList = new UiFixedGridList(2, 5, 300);
	
	private ArrayList<Host> hostsList = new ArrayList<Host>();
	private BroadcastClient broadcastReciever = null;
	private long lastScanTime = 0L;
	
	public IntroJoin(IntroSwitcher introSwitcher, IntroConnected introConnected) {
		this.introSwitcher = introSwitcher;
		this.introConnected = introConnected;
		frmMain = createFrmMain();
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
		gridList.setGridListListener(new GridListAction());
		UiForm frmGrid = new UiForm();
		frmGrid.getLayout().setAlignment(Axis.Y_AXIS, Alignment.BACK);
		frmGrid.getLayout().add(gridList);
		frmGrid.getLayout().add(frmButtons);
		return frmGrid;
	}

	@Override
	public GameInfo getCreatedGameInfo() {
		return null;
	}

	@Override
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public void onFocusGained() {
		setStatus(false, "Searching for open games...");
		createBroadcast();
	}

	@Override
	public void onFocusLost() {
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
				Host host = new Host(msg);
				
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
			broadcastReciever = new BroadcastClient(port, host, 256);
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
	
	private void tryConnecting(int rowIndex) {
		if (rowIndex < hostsList.size()) {
			setStatus(false, "Attempting to connect...");
			Host host = hostsList.get(rowIndex);
			tryConnecting(host);
		}
	}
	
	private void tryConnecting(Host host) {
		if (host.getVersion().isGreaterThan(Params.VERSION)) {
			setStatus(true, "Please download the latest update to join this game");
			return;
		}
		
		if (Params.VERSION.isGreaterThan(host.getVersion())) {
			setStatus(true, "The other game's version is outdated");
			return;
		}
		
		try {
			Network network = Network.connectToServer(host.getAddress(), host.getPort(),
					IdGenerator.getNext());
			introConnected.setNetwork(network);
			introSwitcher.switchToForm(GameIntroForms.CONNECTED);
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
			
			tryConnecting(gridList.getSelectedRowIndex());
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
	
	private class GridListAction implements UiGridList.GridListChange {
		private static final long doubleClickDelta = 400L;
		
		private long lastChangeTime = 0L;
		@Override
		public void onChange(int index, String[] text) {
			if (lastChangeTime + doubleClickDelta > System.currentTimeMillis()) {
				tryConnecting(index);
			}
			lastChangeTime = System.currentTimeMillis();
		}
	}
	
	private class Host {
		private final String addr;
		private final int port;
		private final InetAddress iNetAddr;
		private final Version version;
		
		public String getAddress() { return addr; }
		public int getPort() { return port; }
		public Version getVersion() { return version; }
		
		public Host(BroadcastMsg msg) throws UnknownHostException {
			this(msg.getHost(), msg.getPort(), msg.getVersion());
		}
		
		public Host(String addr, int port, Version version) throws UnknownHostException {
			this.addr = addr;
			this.port = port;
			this.version = version;
			iNetAddr = InetAddress.getByName(addr);
		}
		
		@Override
		public String toString() {
			//TODO iNetAddr.getHostName() never returns on some networks
			String toString = "";
			if (!version.equals(Params.VERSION))
				toString = "[ incompatible game versions ]  ";
			toString += iNetAddr.getHostName();
			return toString;
		}
		
		
	}

}
