package mobile.android.jx.cube;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

public class Main extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		GLSurfaceView glView = new GLSurfaceView(this);
		MyRender myRender = new MyRender();
		glView.setRenderer(myRender);

		setContentView(glView);

	}
}