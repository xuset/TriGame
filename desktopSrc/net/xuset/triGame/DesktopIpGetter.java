package net.xuset.triGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.xuset.triGame.intro.IpGetterIFace;

public class DesktopIpGetter implements IpGetterIFace {
	private static final String errorMsg = "Could not determine local IP";
	
	private InetAddress address = null;
	private String msg = errorMsg;
	
	public DesktopIpGetter() {
		new Thread(new Worker(), "LocalIpFinder").start();
	}

	@Override
	public InetAddress getLocalIP() {
		return address;
	}

	@Override
	public String getError() {
		return msg;
	}
	
	private class Worker implements Runnable {
		
		@Override
		public void run() {
			try {
				address = InetAddress.getLocalHost();
				msg = "";
			} catch (UnknownHostException e) {
				System.err.println("Error: " + errorMsg);
				e.printStackTrace();
			}
		}
		
	}

}
