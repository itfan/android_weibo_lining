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
		// ���������Ϣ����
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		// ���ݵ�ǰWIFI��״̬���Ƿ񱻴򿪣����ø�ѡ���ѡ��״̬
		if (wifiManager.isWifiEnabled())
		{
			sb.append("Wifi�ѿ���\n");
		}
		else
		{
			sb.append("Wifi�ѹر�\n");
		}

		sb.append("MAC��ַ��" + wifiInfo.getMacAddress() + "\n");
		sb.append("������BSSID��" + wifiInfo.getBSSID() + "\n");

		sb.append("IP��ַ��int����" + wifiInfo.getIpAddress() + "\n");
		sb.append("IP��ַ��Hex����" + Integer.toHexString(wifiInfo.getIpAddress())
				+ "\n");
		sb.append("IP��ַ��" + ipIntToString(wifiInfo.getIpAddress()) + "\n");
		sb.append("�����ٶȣ�" + wifiInfo.getLinkSpeed() + "Mbps\n");

		sb.append("\n�����õ���������\n\n");
		for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++)
		{

			WifiConfiguration wifiConfiguration = wifiManager
					.getConfiguredNetworks().get(i);
			sb.append(wifiConfiguration.SSID + ((wifiConfiguration.status == 0) ? "������"
					: "δ����") + "\n");
		}

		tvWifiInfo.setText(sb.toString());

	}

	// ��int���͵�IPת�����ַ�����ʽ��IP
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