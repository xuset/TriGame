package triGame.intro;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objectIO.connections.Connection;
import objectIO.connections.p2pServer.server.ConnectionEvent;
import objectIO.connections.p2pServer.server.P2PServer;
import objectIO.connections.p2pServer.server.ServerConnection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjController;

import tSquare.system.Network;

public class Lobby extends JPanel{
	private static final long serialVersionUID = 2236815803494900637L;
	
	private JLabel lblSize = new JLabel();
	private JButton btnStart = new JButton("Start game");
	private ObjController netController;
	private NetFunction startFunc;
	private Network network;
	private boolean started = false;
	
	Lobby(Network net) {
		network = net;
		netController = new ObjController(net.hub);
		new Thread(netController).start();
		startFunc = new NetFunction(netController, "start", startEvent);
		
		displaySize(net.hub.getAllConnections().size());
		add(lblSize);
		add(Box.createHorizontalStrut(20));
		if (net.isServer)
			hosting(net);
		else
			joining();
	}
	
	private void hosting(Network net) {
		net.getServerInstance().event = connectionEvent;
		btnStart.addMouseListener(startListener);
		add(btnStart);
	}
	
	private void joining() {
		lblSize.setText("Waiting on host to start");
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
			startFunc.sendCall(new MarkupMsg(), Connection.BROADCAST_CONNECTION);
			network.flush();
			shutdown();
		}
		public void mouseEntered(MouseEvent arg0) { }
		public void mouseExited(MouseEvent arg0) { }
		public void mousePressed(MouseEvent arg0) { }
		public void mouseReleased(MouseEvent arg0) { }
	};
	
	private ConnectionEvent connectionEvent = new ConnectionEvent() {
		@Override
		public void onConnect(P2PServer s, ServerConnection c) {
			displaySize(s.connections.size());
		}

		@Override
		public void onDisconnect(P2PServer s, ServerConnection c) {
			displaySize(s.connections.size());
		}
		
		public void onLastDisconnect(P2PServer s) { }
	};
}
