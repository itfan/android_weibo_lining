package mobile.android.jx.control.wifi;

import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

public class ControlWifi extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick_OpenWifiSettings(View view)
	{
		Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		startActivity(intent);

	}

	public void onClick_StartWifi(View view)
	{

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(true);
		
	}

	public void onClick_CloseWifi(View view)
	{
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		if (wifiManager.isWifiEnabled())
			wifiManager.setWifiEnabled(false);

	}
	
	public void onClick_StartScan(View view)
	{
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		
	}
}