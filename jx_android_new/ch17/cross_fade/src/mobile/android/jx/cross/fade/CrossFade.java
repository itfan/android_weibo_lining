package mobile.android.jx.cross.fade;


import android.app.Activity;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CrossFade extends Activity {
	private ImageView ivLamp;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ivLamp = (ImageView) findViewById(R.id.imageview_lamp);
	} 
		
    
	public void onClick_LampOn(View view)
	{
				
		TransitionDrawable drawable = (TransitionDrawable)ivLamp.getDrawable();
		drawable.startTransition(1000);
	}

	public void onClick_LampOff(View view)
	{
		TransitionDrawable drawable = (TransitionDrawable)ivLamp.getDrawable();
		drawable.reverseTransition(1000);
	}
}