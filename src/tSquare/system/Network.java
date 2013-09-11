package tSquare.system;

import java.io.IOException;
import java.net.UnknownHostException;

import objectIO.connection.Hub;
import objectIO.connection.p2pServer.ClientHub;
import objectIO.connection.p2pServer.ServerHub;
import objectIO.netObject.NetObjectController;

public class Network {
	private Hub<?> hub;
	private NetObjectController netController;
	private long userId;
	private boolean isServer;
	
	public Hub<?> getHub() { return hub; }
	public NetObjectController getObjController() { return netController; }
	public long getUserId() { return userId; }
	public boolean isServer() { return isServer; }
	
	private ServerHub server;
	public ServerHub getServerInstance() { return server; }
	
	private ClientHub client;
	public ClientHub getClientInstance() { return client; }
	
	private Network(Hub<?> hub, boolean isServer, long userId) {
		this.hub = hub;
		netController = new NetObjectController(hub);
		this.isServer = isServer;
		this.userId = userId;
	}
	
	public static Network connectToServer(String host, int port, long userId) throws UnknownHostException {
		ClientHub hub = new ClientHub(userId, host, port);
		Network n = new Network(hub, false, userId);
		n.client = hub;
		return n;
	}
	
	public static Network startupServer(int port, long userId) throws IOException {
		ServerHub hub = new ServerHub(userId, port);
		hub.startServer();
		Network n = new Network(hub, true, userId);
		n.server = hub;
		return n;
	}
	
	public boolean waitForClientsToConnect(int numOfClients, int wait) {
		long timeStarted = System.currentTimeMillis();
		while (true) {
			if (getHub().getAllConnections().size() == numOfClients)
				return true;
			if (wait >= 0 && timeStarted + wait < System.currentTimeMillis())
				return false;
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
	}
}
