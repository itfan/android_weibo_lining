package mobile.android.jx.level.list;

import android.app.Activity;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LevelList extends Activity
{
	private ImageView ivLamp;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ivLamp = (ImageView) findViewById(R.id.imageview_lamp);
		ivLamp.setImageLevel(8);
	}

	public void onClick_LampOn(View view)
	{
		// LevelListDrawable levelListDrawable =
		// (LevelListDrawable)ivLamp.getDrawable();
		// levelListDrawable.setLevel(15);
		ivLamp.setImageLevel(15);

	}

	public void onClick_LampOff(View view)
	{
		ivLamp.getDrawable().setLevel(6);

	}
}