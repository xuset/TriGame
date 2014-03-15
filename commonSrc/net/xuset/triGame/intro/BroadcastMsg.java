package net.xuset.triGame.intro;

import java.net.InetAddress;
import java.net.UnknownHostException;

class BroadcastMsg {
	private static final String networkIdentifier = "TriGameBroadcast";
	private static final String delimeter = "/";
	
	private final String host;
	private final int port;
	
	public String getHost() { return host; }
	public int getPort() { return port; }
	
	
	public BroadcastMsg(InetAddress addr, int port) throws UnknownHostException {
		this.port = port;
		host = addr.getHostAddress();
	}
	
	public BroadcastMsg(String recievedBroadcast) throws IllegalArgumentException{
		String[] split = recievedBroadcast.split(delimeter);
		if (split.length < 4)
			throwException();
		
		String ident = split[0];
		String strHost = split[1];
		String strPort = split[2];
		int intPort = 0;
		
		if (!ident.equals(networkIdentifier))
			throwException();
		
		try {
			intPort = Integer.parseInt(strPort);
		} catch (NumberFormatException ex) {
			throwException();
		}
		
		host = strHost;
		port = intPort;
	}
	
	private void throwException() {
		throw new IllegalArgumentException("Message is not in a valid format");
	}
	
	@Override
	public String toString() {
		return networkIdentifier + delimeter + host + delimeter + port + delimeter;
	}
}
