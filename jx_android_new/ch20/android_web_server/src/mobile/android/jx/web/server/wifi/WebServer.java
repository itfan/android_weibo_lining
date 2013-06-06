package mobile.android.jx.web.server.wifi;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.TextView;

public class WebServer extends Activity
{
	private ServerSocket serverSocket;

	private WifiManager wifiManager;
	private WifiInfo wifiInfo;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		wifiInfo = wifiManager.getConnectionInfo();
		TextView textView = (TextView) findViewById(R.id.textview);
		textView.setText("访问地址\n" + "http://"
				+ ipIntToString(wifiInfo.getIpAddress()) + ":4321");

		new ServerThread().start();
	}

	private String getHtml()
	{
		String result = "";
		try
		{
			InputStream is = getResources().getAssets().open("info.html");
			byte[] buffer = new byte[1024];
			int count = is.read(buffer);
			result = new String(buffer, 0, count, "utf-8");
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return result;
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

	class ServerThread extends Thread
	{
		public void run()
		{
			try
			{
				serverSocket = new ServerSocket(4321);

				while (true)
				{

					Socket socket = serverSocket.accept();

					String html = getHtml();
					String mac = "";
					String ip = "";
					String wifiStatus = "";
					String speed = "";
					String usingNetwork = "";

					mac = wifiInfo.getMacAddress();
					ip = ipIntToString(wifiInfo.getIpAddress());
					if (wifiManager.isWifiEnabled())
					{
						wifiStatus = "Wifi已开启";
					}
					else
					{
						wifiStatus = "Wifi已关闭\n";
					}

					speed = wifiInfo.getLinkSpeed() + "Mbps";
					for (int i = 0; i < wifiManager.getConfiguredNetworks()
							.size(); i++)
					{

						WifiConfiguration wifiConfiguration = wifiManager
								.getConfiguredNetworks().get(i);
						if (wifiConfiguration.status == 0)
						{
							usingNetwork = wifiConfiguration.SSID.replaceAll(
									"\"", "");
							break;
						}

					}

					html = html.replaceAll("#mac#", mac).replaceAll("#ip#", ip)
							.replaceAll("#wifi_status#", wifiStatus)
							.replaceAll("#speed#", speed)
							.replaceAll("#using_network#", usingNetwork);
					html = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: "
							+ html.getBytes("utf-8").length + "\r\n\r\n" + html;
					OutputStream os = socket.getOutputStream();
					os.write(html.getBytes("utf-8"));
					os.flush();
					socket.close();

				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}
}