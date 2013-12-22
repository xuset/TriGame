package triGame.intro;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import objectIO.connections.Connection;
import objectIO.connections.sockets.p2pServer.server.ConnectionEvent;
import objectIO.connections.sockets.p2pServer.server.P2PServer;
import objectIO.connections.sockets.p2pServer.server.ServerConnection;
import objectIO.markupMsg.MarkupMsg;
import objectIO.markupMsg.MsgAttribute;
import objectIO.netObject.NetFunction;
import objectIO.netObject.NetFunctionEvent;
import objectIO.netObject.ObjController;
import tSquare.system.Network;
import triGame.game.GameMode.GameType;

class Lobby extends JPanel{
	private static final long serialVersionUID = 2236815803494900637L;
	
	private final JPanel pnlMain = new JPanel();
	private final JLabel lblSize = new JLabel();
	private final JButton btnStart = new JButton("Start game");
	private final JLabel lblHostInfo = new JLabel();
	private final ObjController netController;
	private final NetFunction startFunc;
	private final Network network;
	private final GameInfo gameInfo;
	private boolean started = false;
	
	Lobby(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		network = gameInfo.network;
		netController = new ObjController(network.hub);
		startFunc = new NetFunction(netController, "start", startEvent);
		new Thread(netController).start();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		displaySize(network.hub.getAllConnections().size());
		pnlMain.add(Box.createHorizontalGlue());
		pnlMain.add(lblSize);
		pnlMain.add(Box.createHorizontalGlue());
		if (network.isServer)
			hosting();
		else
			joining();
		add(lblHostInfo);
		add(Box.createVerticalGlue());
		add(pnlMain);
	}
	
	void waitForPlayers() {
		while (started == false) {
			if (!network.isServer && !network.getClientInstance().isConnectedToServer()) {
				lblSize.setForeground(Color.red);
				lblSize.setText("Disconnected from host!");
			}
			try { Thread.sleep(10); } catch(Exception ex) { }
		}
	}
	
	private void hosting() {
		network.getServerInstance().event = connectionEvent;
		btnStart.addActionListener(startListener);
		pnlMain.add(btnStart);
		lblHostInfo.setText("Your ip:port = " + getIp() + ":" + network.getServerInstance().getPort());
		lblHostInfo.setAlignmentX(CENTER_ALIGNMENT);
	}
	
	private void joining() {
		lblSize.setText("Waiting on host to start");
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
			int ordinal = args.getAttribute("Gametype").getInt();
			GameType type = GameType.values()[ordinal];
			gameInfo.mode = type;
			shutdown();
			return null;
		}
		public void returnedFunc(MarkupMsg args, Connection c) { }
	};
	
	private ActionListener startListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			MarkupMsg msg = new MarkupMsg();
			msg.addAttribute(new MsgAttribute("Gametype").set(gameInfo.mode.ordinal()));
			startFunc.sendCall(msg, Connection.BROADCAST_CONNECTION);
			network.flush();
			shutdown();
		}
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
	
	private String getIp() {
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
			    NetworkInterface current = interfaces.nextElement();
			    
			    if (!current.isUp() || current.isLoopback() || current.isVirtual())
			    	continue;
			    
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress currentAddr = addresses.nextElement();
			        
			        if (currentAddr.isLoopbackAddress())
			        	continue;
			        
			        if (currentAddr instanceof Inet4Address) {
			        	return currentAddr.getHostAddress();
			        }
			    }
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return "ip not found";
	}
}
