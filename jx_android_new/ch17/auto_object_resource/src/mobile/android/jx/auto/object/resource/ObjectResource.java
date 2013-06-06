package mobile.android.jx.auto.object.resource;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ObjectResource extends Activity
{
	private MyObject myObject;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		myObject = (MyObject) getLastNonConfigurationInstance();
		if (myObject == null)
			myObject = new MyObject();
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		Log.d("method", "onRetainNonConfigurationInstance");
		return myObject;
	} 

	public void onClick_SetObjectValue(View view)
	{
		myObject.id = 1;
		myObject.name = "ÀîÄþ";
	}

	public void onClick_ShowObjectValue(View view)
	{
		if (myObject != null)
		{
			Toast.makeText(this,
					"id£º" + myObject.id + "\nname:" + myObject.name,
					Toast.LENGTH_LONG).show();
  
		}
	}
}