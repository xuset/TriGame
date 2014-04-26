package net.xuset.triGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.xuset.triGame.intro.IpGetterIFace;

public class WifiIpGetter implements IpGetterIFace {
	private final WifiManager wifiManager;
	
	private InetAddress ipAddress;
	
	
	public WifiIpGetter(Context context) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		ipAddress = determineIpFromWifi();
		if (ipAddress == null)
			fallbackOnError();
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
		} catch (RuntimeException ex) {
			System.err.println("An unknown error occured while trying to determine the local ip");
			ex.printStackTrace();
			ipAddress = null;
		}
	}
	
	private InetAddress determineIpFromWifi() {
		if (!wifiManager.isWifiEnabled())
			return null;
		
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		
		if (ip == 0)
			return null;
		
		String ipString = String.format(
				   "%d.%d.%d.%d",
				   (ip & 0xff),
				   (ip >> 8 & 0xff),
				   (ip >> 16 & 0xff),
				   (ip >> 24 & 0xff));
		
		try {
			return InetAddress.getByName(ipString);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

	@Override
	public InetAddress getLocalIP() {
		return ipAddress;
	}

}
