package mobile.android.jx.simple_resource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SimpleResource extends Activity
{
	private int intValue;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (savedInstanceState != null)
		{
			intValue = savedInstanceState.getInt("int_value");
		}
		Log.d("method", "onCreate");

	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		Log.d("method", "onSaveInstanceState");
		outState.putString("name", "ÀîÄþ");
		outState.putInt("int_value", intValue);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		Log.d("method", "onRestoreInstanceState");
		if (savedInstanceState != null)
			setTitle(savedInstanceState.getString("name"));
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void onClick_ShowActivity(View view)
	{
		Intent intent = new Intent(this, MyActivity.class);

		startActivity(intent);
	}

	public void onClick_SetValue(View view)
	{
		intValue = 100;
	}

	public void onClick_ShowValue(View view)
	{
		Toast.makeText(this, String.valueOf(intValue), Toast.LENGTH_LONG)
				.show();
	}
}