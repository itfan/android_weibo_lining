package mobile.android.jx.bluetooth.device;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import mobile.android.ch13.connect.bluetooth.device.R;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity implements OnItemClickListener
{
	private ListView lvDevices;
	private BluetoothAdapter bluetoothAdapter;
	private List<String> bluetoothDevices = new ArrayList<String>();
	private ArrayAdapter<String> arrayAdapter;
	private final UUID MY_UUID = UUID
			.fromString("db764ac8-7f26-4b08-aafe-59d03c27bae3");
	private final String NAME = "Bluetooth_Socket";
	private AcceptThread acceptThread;
	private BluetoothSocket clientSocket;
	private BluetoothDevice device;
	private OutputStream os;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		lvDevices = (ListView) findViewById(R.id.lvDevices);

		Set<BluetoothDevice> pairedDevices = bluetoothAdapter
				.getBondedDevices();

		if (pairedDevices.size() > 0)
		{
			for (BluetoothDevice device : pairedDevices)
			{
				bluetoothDevices.add(device.getName() + ":"
						+ device.getAddress() + "\n");
			}
		}
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				bluetoothDevices);

		lvDevices.setAdapter(arrayAdapter);
		lvDevices.setOnItemClickListener(this);
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(receiver, filter);

		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(receiver, filter);
		if (!bluetoothAdapter.isEnabled())
		{
			bluetoothAdapter.enable();
		}
		acceptThread = new AcceptThread();
		acceptThread.start();
	}

	public void onClick_Search(View view)
	{
		setProgressBarIndeterminateVisibility(true);
		setTitle("正在扫描...");

		if (bluetoothAdapter.isDiscovering())
		{
			bluetoothAdapter.cancelDiscovery();
		}
		bluetoothAdapter.startDiscovery();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		String s = arrayAdapter.getItem(position);
		String address = s.substring(s.indexOf(":") + 1).trim();

		try
		{
			if (bluetoothAdapter.isDiscovering())
			{
				this.bluetoothAdapter.cancelDiscovery();
			}

			try
			{

				if (device == null)
				{
					device = bluetoothAdapter.getRemoteDevice(address);
				}

				if (clientSocket == null)
				{
					clientSocket = device
							.createRfcommSocketToServiceRecord(MY_UUID);
					clientSocket.connect();
					os = clientSocket.getOutputStream();
				}

			}
			catch (IOException e)
			{

			}
			if (os != null)
			{
				FileInputStream fis = new FileInputStream("/sdcard/video.3gp");
				byte[] buffer = new byte[8192];
				int count = 0;
				int totalCount = 0;
				while ((count = fis.read(buffer)) > 0)
				{
					os.write(buffer, 0, count);
					totalCount += count;
					Log.d("total_count：", String.valueOf(totalCount));
				}

				fis.close();

				Toast.makeText(this, "文件传输成功.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(this, "文件传输失败.", Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

		}

	}

	private final BroadcastReceiver receiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{

				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (device.getBondState() != BluetoothDevice.BOND_BONDED)
				{
					bluetoothDevices.add(device.getName() + ":"
							+ device.getAddress() + "\n");
					arrayAdapter.notifyDataSetChanged();
				}

			}
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
			{
				setProgressBarIndeterminateVisibility(false);
				setTitle("连接蓝牙设备");

			}
		}
	};
	

	private class AcceptThread extends Thread
	{
		private BluetoothServerSocket serverSocket;
		private BluetoothSocket socket;
		private InputStream is;


		public AcceptThread()
		{

			try
			{

				serverSocket = bluetoothAdapter
						.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);

			}
			catch (IOException e)
			{

			}

		}

		public void run()
		{

			try
			{
				socket = serverSocket.accept();

				is = socket.getInputStream();
				FileOutputStream fos = new FileOutputStream(
						"/sdcard/video_bluetooth.3gp");
				byte[] buffer = new byte[8192];
				int count = 0;
				int totalCount = 0;
				while ((count = is.read(buffer, 0, buffer.length)) >= 0)
				{

					fos.write(buffer, 0, count);
					totalCount += count;
					Log.d("total_count", String.valueOf(totalCount));
				}

			}
			catch (Exception e)
			{
				
			}

		}

	}

}
