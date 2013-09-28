package tSquare.system;

import java.io.IOException;
import java.net.UnknownHostException;

import objectIO.connections.Hub;
import objectIO.connections.p2pServer.client.ClientHub;
import objectIO.connections.p2pServer.server.P2PServer;
import objectIO.netObject.NetObjectController;

public class Network {
	private Hub<?> hub;
	private NetObjectController netController;
	private long userId;
	private boolean isServer = false;
	
	public Hub<?> getHub() { return hub; }
	public NetObjectController getObjController() { return netController; }
	public long getUserId() { return userId; }
	public boolean isServer() { return isServer; }
	
	private P2PServer server;
	public P2PServer getServerInstance() { return server; }
	
	private ClientHub client;
	public ClientHub getClientInstance() { return client; }
	
	private Network(Hub<?> hub, long userId) {
		this.hub = hub;
		netController = new NetObjectController(hub);
		this.userId = userId;
	}
	
	private Network(P2PServer server, Hub<?> hub, long userId) {
		this(hub, userId);
		this.server = server;
		this.hub = hub;
		isServer = true;
	}
	
	public static Network connectToServer(String host, int port, long userId) throws UnknownHostException, IOException{
		ClientHub hub = new ClientHub(host, port, userId);
		Network n = new Network(hub, userId);
		n.client = hub;
		return n;
	}
	
	public static Network startupServer(int port) throws IOException {
		P2PServer server = new P2PServer(port);
		server.accepter.start();
		ClientHub hub = new ClientHub("127.0.0.1", port, 3l);
		Network n = new Network(server, hub, 3l);
		n.client = hub;
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
	
	public void disconnect() { //TODO add shutdown() method to Hub<>
		client.shutdown();
		if (server != null)
			server.shutdown();
		client = null;
		server = null;
		hub = null;
	}
}
