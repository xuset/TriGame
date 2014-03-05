package net.xuset.triGame.intro.join;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import net.xuset.objectIO.util.HostCheckerPort;
import net.xuset.objectIO.util.HostFinder;
import net.xuset.tSquare.imaging.IGraphics;
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

public class IntroJoin extends UiForm implements IntroForm{
	
	private final IntroSwitcher introSwitcher;
	private final UiButton btnRefresh = new UiButton("Refresh");
	private final UiButton btnConnect = new UiButton("Connect");
	private final UiButton btnHost = new UiButton("Host new game");
	private final UiLabel lblStatus = new UiLabel("Click 'refresh' to find games");
	private final UiFixedGridList gridList = new UiFixedGridList(3, 5, 300);
	
	private HostFinder hostFinder = null;
	private List<InetAddress> gameAddresses = null;
	private Network network = null;
	
	public IntroJoin(IntroSwitcher introSwitcher) {
		this.introSwitcher = introSwitcher;
		UiQueueLayout layout = new UiQueueLayout(5, 20, this);
		setLayout(layout);
		
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
		return this;
	}
	
	@Override
	public void draw(IGraphics g) {
		if (hostFinder != null && !hostFinder.isScanning()) {
			gameAddresses = hostFinder.createHostsList();
			hostFinder = null;
			
			lblStatus.setForeground(TsColor.black);
			if (gameAddresses.isEmpty())
				lblStatus.setText("No games found. Try refreshing");
			else
				lblStatus.setText("Found " + gameAddresses.size() + " games");
			
			gridList.clearRows();
			for (int i = 0; i < gameAddresses.size(); i++) {
				InetAddress addr = gameAddresses.get(i);
				gridList.addRow("" + (i + 1), addr.getHostName(), addr.getHostAddress());
			}
		}
		super.draw(g);
	}
	
	private class BtnConnectAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			
			try {
				int selected = gridList.getSelectedRowIndex();
				if (selected < gameAddresses.size()) {
					lblStatus.setForeground(TsColor.black);
					lblStatus.setText("Attempting to connect...");
					InetAddress addr = gameAddresses.get(selected);
					network = Network.connectToServer(addr.getHostAddress(), 3000, IdGenerator.getNext());
					lblStatus.setText("Connected. Waiting on host to start.");
				}
			} catch (UnknownHostException e) {
				lblStatus.setForeground(TsColor.red);
				lblStatus.setText("Error: Server not found");
				e.printStackTrace();
			} catch (IOException e) {
				lblStatus.setForeground(TsColor.red);
				lblStatus.setText("Error: Could not connect");
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
	
	private class BtnRefreshAction implements Observer.Change<TsMouseEvent> {
		@Override
		public void observeChange(TsMouseEvent t) {
			if (t.action != MouseAction.PRESS)
				return;
			

			lblStatus.setForeground(TsColor.black);
			lblStatus.setText("Searching for open games...");
			try {
				hostFinder = new HostFinder(new HostCheckerPort(3000));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

}
