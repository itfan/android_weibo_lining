package mobile.android.control.bluetooth.device;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick_Enable_Bluetooth(View view)
	{

		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableIntent, 1);
		// BluetoothAdapter bluetoothAdapter = BluetoothAdapter
		// .getDefaultAdapter();

		// bluetoothAdapter.enable();

	}

	public void onClick_Disable_Bluetooth(View view)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		bluetoothAdapter.disable();

	}

	public void onClick_Bluetooth_State(View view)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();

		if (bluetoothAdapter.isEnabled())
			Toast.makeText(this, "蓝牙设备已开启.", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(this, "蓝牙设备已关闭.", Toast.LENGTH_LONG).show();
	}

}