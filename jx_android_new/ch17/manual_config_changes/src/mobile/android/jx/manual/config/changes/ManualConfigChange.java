package mobile.android.jx.manual.config.changes;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

public class ManualConfigChange extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
		
	}
 
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{

		super.onConfigurationChanged(newConfig);
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
		{
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
		
		if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO)
		{
			Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
		}
		else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES)
		{
			Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
		}

	}

}