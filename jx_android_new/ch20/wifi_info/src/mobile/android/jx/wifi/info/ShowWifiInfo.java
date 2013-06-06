package mobile.android.jx.wifi.info;

import java.net.Inet4Address;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class ShowWifiInfo extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView tvWifiInfo = (TextView) findViewById(R.id.textview_wifi_info);
		StringBuffer sb = new StringBuffer();

		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 获得连接信息对象
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		// 根据当前WIFI的状态（是否被打开）设置复选框的选中状态
		if (wifiManager.isWifiEnabled())
		{
			sb.append("Wifi已开启\n");
		}
		else
		{
			sb.append("Wifi已关闭\n");
		}

		sb.append("MAC地址：" + wifiInfo.getMacAddress() + "\n");
		sb.append("接入点的BSSID：" + wifiInfo.getBSSID() + "\n");

		sb.append("IP地址（int）：" + wifiInfo.getIpAddress() + "\n");
		sb.append("IP地址（Hex）：" + Integer.toHexString(wifiInfo.getIpAddress())
				+ "\n");
		sb.append("IP地址：" + ipIntToString(wifiInfo.getIpAddress()) + "\n");
		sb.append("连接速度：" + wifiInfo.getLinkSpeed() + "Mbps\n");

		sb.append("\n已配置的无线网络\n\n");
		for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++)
		{

			WifiConfiguration wifiConfiguration = wifiManager
					.getConfiguredNetworks().get(i);
			sb.append(wifiConfiguration.SSID + ((wifiConfiguration.status == 0) ? "已连接"
					: "未连接") + "\n");
		}

		tvWifiInfo.setText(sb.toString());

	}

	// 将int类型的IP转换成字符串形式的IP
	private String ipIntToString(int ip)
	{
		try
		{
			byte[] bytes = new byte[4];
			bytes[0] = (byte) (0xff & ip);
			bytes[1] = (byte) ((0xff00 & ip) >> 8);
			bytes[2] = (byte) ((0xff0000 & ip) >> 16);
			bytes[3] = (byte) ((0xff000000 & ip) >> 24);
			return Inet4Address.getByAddress(bytes).getHostAddress();
		}
		catch (Exception e)
		{
			return "";
		}
	}

}