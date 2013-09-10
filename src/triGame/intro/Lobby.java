package triGame.intro;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objectIO.connection.AbstractConnection;
import objectIO.connection.Connection;
import objectIO.connection.p2pServer.ConnectionEvent;
import objectIO.connection.p2pServer.P2PConnection;
import objectIO.connection.p2pServer.P2PHub;
import objectIO.markupMsg.MarkupMsg;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.NetObjectController;

import tSquare.system.Network;

public class Lobby extends JPanel{
	private static final long serialVersionUID = 2236815803494900637L;
	
	private JLabel lblSize = new JLabel();
	private JButton btnStart = new JButton("Start game");
	private NetObjectController netController;
	private NetFunction startFunc;
	private boolean started = false;
	
	Lobby(Network net) {
		netController = new NetObjectController(net.getHub());
		new Thread(netController).start();
		startFunc = new NetFunction(netController, "start", startEvent);
		
		displaySize(net.getHub().getAllConnections().size() + 1);
		add(lblSize);
		add(Box.createHorizontalStrut(20));
		if (net.isServer())
			hosting(net);
		else
			joining();
	}
	
	private void hosting(Network net) {
		net.getServerInstance().connectionEvent = connectionEvent;
		btnStart.addMouseListener(startListener);
		add(btnStart);
	}
	
	private void joining() {
		add(new JLabel("Waiting on host to start"));
	}
	
	void waitForPlayers() {
		while (started == false) {
			try { Thread.sleep(10); } catch(Exception ex) { }
		}
	}
	
	private void shutdown() {
		started = true;
		netController.stopRunning();
	}
	
	private void displaySize(int size) {
		if (size > 1)
			lblSize.setText(size + " players joined");
		else
			lblSize.setText("1 player joined");
	}
	
	private NetFunctionEvent startEvent = new NetFunctionEvent() {
		@Override
		public MarkupMsg calledFunc(MarkupMsg args, Connection c) {
			shutdown();
			return null;
		}
		public void returnedFunc(MarkupMsg args, Connection c) { }
	};
	
	private MouseListener startListener = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent arg0) {
			startFunc.sendCall(new MarkupMsg(), AbstractConnection.BROADCAST_CONNECTION);
			shutdown();
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private ConnectionEvent connectionEvent = new ConnectionEvent() {
		@Override
		public void onNewConnection(P2PHub<?> hub, P2PConnection connection) {
			displaySize(hub.getAllConnections().size() + 1);
		}

		@Override
		public void onDisconnection(P2PHub<?> hub, P2PConnection connection) {
			displaySize(hub.getAllConnections().size() + 1);
		}
	};
}
