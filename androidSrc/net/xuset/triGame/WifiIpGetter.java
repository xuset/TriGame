package net.xuset.triGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.xuset.triGame.intro.IpGetterIFace;

public class WifiIpGetter implements IpGetterIFace {
	private InetAddress ipAddress;
	
	public WifiIpGetter(Context context) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		
		if (ip == 0) {
			fallbackOnError();
			return;
		}
		
		String ipString = String.format(
				   "%d.%d.%d.%d",
				   (ip & 0xff),
				   (ip >> 8 & 0xff),
				   (ip >> 16 & 0xff),
				   (ip >> 24 & 0xff));
		
		try {
			ipAddress = InetAddress.getByName(ipString);
		} catch (UnknownHostException e) {
			fallbackOnError();
		}
	}
	
	private void fallbackOnError() {
		System.err.println("Failed to resolve wifi ip address.");
		try {
			ipAddress = InetAddress.getLocalHost();
			if (ipAddress.isLoopbackAddress())
				ipAddress = null;
		} catch (UnknownHostException e) {
			System.err.println("Failed to resolve local host address. using loop back");
			ipAddress = null;
			e.printStackTrace();
		}
	}
	

	@Override
	public InetAddress getLocalIP() {
		return ipAddress;
	}

}
