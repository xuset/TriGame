package tSquare.system;

import java.io.IOException;
import java.net.UnknownHostException;

import objectIO.connections.Hub;
import objectIO.connections.p2pServer.client.ClientHub;
import objectIO.connections.p2pServer.server.P2PServer;
import objectIO.netObject.ObjController;

public class Network {
	public final Hub<?> hub;
	public final ObjController objController;
	public final long userId;
	public final boolean isServer;
	
	private P2PServer server = null;
	public P2PServer getServerInstance() { return server; }
	
	private ClientHub client = null;
	public ClientHub getClientInstance() { return client; }
	
	private Network(Hub<?> hub, long userId, boolean isServer) {
		this.hub = hub;
		objController = new ObjController(hub);
		this.userId = userId;
		this.isServer = isServer;
	}
	
	private Network(P2PServer server, Hub<?> hub, long userId) {
		this(hub, userId, true);
		this.server = server;
	}
	
	public static Network connectToServer(String host, int port, long userId) throws UnknownHostException, IOException{
		ClientHub hub = new ClientHub(host, port, userId);
		Network n = new Network(hub, userId, false);
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
			if (hub.getAllConnections().size() == numOfClients)
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
	}
	
	public void flush() {
		client.flush();
	}
}
