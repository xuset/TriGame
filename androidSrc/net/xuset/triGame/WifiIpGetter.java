package net.xuset.triGame;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import net.xuset.triGame.intro.IpGetterIFace;

public class WifiIpGetter implements IpGetterIFace {
	private static final String errResolveMsg =
			"Failed to resolve wifi ip address.";
	
	private final WifiManager wifiManager;
	private String errorMsg = "";
	
	public WifiIpGetter(Context context) {
		wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	public InetAddress getLocalIP() {
		if (isWifiConnected()) {
			InetAddress ip;
			ip = determineIpFromWifi();
			if (ip == null) {
				errorMsg = errResolveMsg;
				ip = fallbackOnError();
			}
			return ip;
		} else {
			errorMsg = "Wifi is not connected";
		}
		
		return null;
			
	}
	
	@Override
	public String getError() {
		return errorMsg;
	}
	
	private boolean isWifiConnected() {
		return wifiManager.isWifiEnabled() &&
				wifiManager.getConnectionInfo().getIpAddress() != 0;
	}
	
	private InetAddress fallbackOnError() {
		System.err.println("Error: " + errResolveMsg);
		
		try {
			InetAddress ip = InetAddress.getLocalHost();
			if (!ip.isLoopbackAddress())
				return ip;
		} catch (UnknownHostException e) {
			System.err.println("Failed to resolve local host address. using loop back");
			e.printStackTrace();
		} catch (RuntimeException ex) {
			System.err.println("An unknown error occured while trying to determine the local ip");
			ex.printStackTrace();
		}

		return null;
	}
	
	private InetAddress determineIpFromWifi() {
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ip = wifiInfo.getIpAddress();
		
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

}
