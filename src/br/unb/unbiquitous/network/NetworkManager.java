package br.unb.unbiquitous.network;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;

public class NetworkManager {

	final private WifiManager wifiManager;
	private boolean wasWifiEnabled;
	
	public NetworkManager(Activity activity) {
		wifiManager = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
	}
	
	public void setWifiEnabled(boolean enabled) {
		if(wifiManager.isWifiEnabled()) {
			wasWifiEnabled = true;
		} else {
			wifiManager.setWifiEnabled(enabled);
			wasWifiEnabled = false;
		}
	}
	
	public void tearDown() {
		if(!wasWifiEnabled){
			wifiManager.setWifiEnabled(false);
		}
	}
	
}
