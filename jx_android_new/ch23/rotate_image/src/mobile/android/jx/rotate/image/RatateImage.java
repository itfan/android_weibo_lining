package mobile.android.jx.rotate.image;

import android.app.Activity;
import android.os.Bundle;

public class RatateImage extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}
}