package net.xuset.tSquare.system;

import java.io.IOException;
import java.net.UnknownHostException;

import net.xuset.objectIO.connections.sockets.InetCon;
import net.xuset.objectIO.connections.sockets.InetHub;
import net.xuset.objectIO.connections.sockets.OfflineHub;
import net.xuset.objectIO.connections.sockets.groupNet.client.GroupClientHub;
import net.xuset.objectIO.connections.sockets.groupNet.server.GroupNetServer;
import net.xuset.objectIO.netObject.StandardObjUpdater;
import net.xuset.objectIO.netObject.NetObjUpdater;
import net.xuset.objectIO.netObject.OfflineObjController;



public class Network {
	public final InetHub<? extends InetCon> hub;
	public final NetObjUpdater objController;
	public final long userId;
	public final boolean isServer;
	
	private GroupNetServer server = null;
	public GroupNetServer getServerInstance() { return server; }
	
	private GroupClientHub client = null;
	public GroupClientHub getClientInstance() { return client; }
	
	private Network() {
		isServer = true;
		userId = 3l;
		hub = new OfflineHub(userId);
		objController = new OfflineObjController();
	}
	
	private Network(InetHub<? extends InetCon> hub, long userId, boolean isServer) {
		this.hub = hub;
		objController = new StandardObjUpdater(hub);
		this.userId = userId;
		this.isServer = isServer;
	}
	
	private Network(GroupNetServer server, InetHub<? extends InetCon> hub, long userId) {
		this(hub, userId, true);
		this.server = server;
	}
	
	public static Network connectToServer(String host, int port, long userId) throws UnknownHostException, IOException{
		GroupClientHub hub = new GroupClientHub(host, port, userId);
		Network n = new Network(hub, userId, false);
		n.client = hub;
		return n;
	}
	
	public static Network startupServer(int port) throws IOException {
		GroupNetServer server = new GroupNetServer(3L, port);
		GroupClientHub hub = new GroupClientHub("127.0.0.1", server.getPort(), 3l);
		Network n = new Network(server, hub, 3l);
		n.client = hub;
		return n;
	}
	
	public static Network createOffline() {
		return new Network();
	}
	
	public boolean waitForClientsToConnect(int numOfClients, int wait) {
		long timeStarted = System.currentTimeMillis();
		while (true) {
			if (hub.getConnectionCount() == numOfClients)
				return true;
			if (wait >= 0 && timeStarted + wait < System.currentTimeMillis())
				return false;
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
	}
	
	public void disconnect() {
		if (client != null)
			client.shutdown();
		if (server != null)
			server.shutdown();
		client = null;
		server = null;
	}
	
	public void flush() {
		if (client != null) {
			for (int i = 0; i < client.getConnectionCount(); i++) {
				client.getConnectionByIndex(i).flush();
			}
		}
	}
}
