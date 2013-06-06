package mobile.android.jx.draw2d;

import android.app.Activity;
import android.os.Bundle;

public class Draw2D extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}
}