package mobile.android.jx.discovered.bluetooth.device;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.IBluetooth;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SearchBluetooth extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onClick_Discovered(View view)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		try
		{

			Field mService = bluetoothAdapter.getClass().getDeclaredField(
					"mService");
			mService.setAccessible(true);
			IBluetooth bluetooth = (IBluetooth) mService.get(bluetoothAdapter);
			bluetooth.setDiscoverableTimeout(120);

			bluetooth.setScanMode(
					BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 120);
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}

	public void onClick_Timeout(View view)
	{
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		try
		{

			Field mService = bluetoothAdapter.getClass().getDeclaredField(
					"mService");
			mService.setAccessible(true);
			IBluetooth bluetooth = (IBluetooth) mService.get(bluetoothAdapter);
			Toast.makeText(this,
					"≥¨ ± ±º‰£∫" + bluetooth.getDiscoverableTimeout() + "√Î",
					Toast.LENGTH_LONG).show();

		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

	}
}