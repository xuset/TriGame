package net.xuset.triGame.intro.join;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import net.xuset.objectIO.util.HostCheckerPort;
import net.xuset.objectIO.util.HostFinder;
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
import net.xuset.triGame.game.GameMode.GameType;
import net.xuset.triGame.intro.GameIntroForms;
import net.xuset.triGame.intro.IntroForm;
import net.xuset.triGame.intro.IntroSwitcher;

public class IntroJoin implements IntroForm{
	
	private final IntroSwitcher introSwitcher;
	private final UiForm frmMain = new UiForm();
	private final UiButton btnRefresh = new UiButton("Refresh");
	private final UiButton btnConnect = new UiButton("Connect");
	private final UiButton btnHost = new UiButton("Host new game");
	private final UiLabel lblStatus = new UiLabel("");
	private final UiFixedGridList gridList = new UiFixedGridList(3, 5, 300);
	
	private InetAddress serverAddress = null;
	private HostFinder hostFinder = null;
	private List<InetAddress> gameAddresses = null;
	private Network network = null;
	private boolean shouldStartNewScan = false;
	
	public IntroJoin(IntroSwitcher introSwitcher) {
		this.introSwitcher = introSwitcher;
		UiQueueLayout layout = new UiQueueLayout(5, 20, frmMain);
		frmMain.setLayout(layout);
		
		btnConnect.addMouseListener(new BtnConnectAction());
		btnHost.addMouseListener(new BtnHostAction());
		btnRefresh.addMouseListener(new BtnRefreshAction());
		UiForm frmButtons = new UiForm();
		frmButtons.getLayout().setOrientation(Axis.Y_AXIS);
		frmButtons.getLayout().setAlignment(Axis.X_AXIS, Alignment.CENTER);
		frmButtons.getLayout().add(btnRefresh);
		frmButtons.getLayout().add(btnConnect);
		
		UiForm frmGrid = new UiForm();
		frmGrid.getLayout().setAlignment(Axis.Y_AXIS, Alignment.BACK);
		frmGrid.getLayout().add(gridList);
		frmGrid.getLayout().add(frmButtons);
		
		layout.setOrientation(Axis.Y_AXIS);
		layout.setAlignment(Axis.X_AXIS, Alignment.CENTER);
		layout.add(lblStatus);
		layout.add(frmGrid);
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
	public UiComponent getForm() {
		return frmMain;
	}

	@Override
	public void onFocusGained() {
		startHostsScan();
	}

	@Override
	public void onFocusLost() {
		if (hostFinder != null) {
			hostFinder.cancelScan();
			hostFinder = null;
		}
	}

	@Override
	public void update() {
		if (network == null && serverAddress != null)
			tryConnecting();
		if (hostFinder != null && !hostFinder.isScanning())
			fillHostsList();
		if (shouldStartNewScan)
			startHostsScan();
	}
	
	private void tryConnecting() {
		try {
			network = Network.connectToServer(serverAddress.getHostAddress(),
					3000, IdGenerator.getNext());
			lblStatus.setText("Connected. Waiting on host to start.");
		} catch (UnknownHostException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			setStatus(true, "Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void fillHostsList() {
		gameAddresses = hostFinder.createHostsList();
		hostFinder = null;
		
		lblStatus.setForeground(TsColor.black);
		if (gameAddresses.isEmpty())
			lblStatus.setText("No games found. Try refreshing");
		else
			lblStatus.setText("Found " + gameAddresses.size() + " games");
		
		serverAddress = null;
		gridList.clearRows();
		for (int i = 0; i < gameAddresses.size(); i++) {
			InetAddress addr = gameAddresses.get(i);
			gridList.addRow("" + (i + 1), addr.getHostName(), addr.getHostAddress());
		}
	}
	
	private void setStatus(boolean isError, String status) {
		lblStatus.setForeground(isError ? TsColor.red : TsColor.black);
		lblStatus.setText(status);
	}
	
	private void startHostsScan() {
		shouldStartNewScan = false;
		setStatus(false, "Scanning for open games...");
		if (hostFinder != null && hostFinder.isScanning())
			hostFinder.cancelScan();
		new HostsScanner();
	}
	
	private class BtnConnectAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			
			int selected = gridList.getSelectedRowIndex();
			if (selected < gameAddresses.size()) {
				setStatus(false, "Attempting to connect...");
				serverAddress = gameAddresses.get(selected);
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
			shouldStartNewScan = true;
		}
	}
	
	private class HostsScanner extends Thread {
		public HostsScanner() {
			start();
		}
		
		@Override
		public void run() {
			try {
				InetAddress local = InetAddress.getLocalHost();
				if (!local.isLoopbackAddress())
					hostFinder = new HostFinder(new HostCheckerPort(3000), 20, local);
				else
					setStatus(true, "Error: could not find network");
			} catch (UnknownHostException e) {
				e.printStackTrace();
				setStatus(true, "Error: " + e.getMessage());
			}
		}
	}

}
