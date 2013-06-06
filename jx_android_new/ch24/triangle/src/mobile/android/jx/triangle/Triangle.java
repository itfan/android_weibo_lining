package mobile.android.jx.triangle;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Triangle extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new GLSurfaceView(this);
		TriangleRender triangleRender = new TriangleRender();
		glView.setRenderer(triangleRender);
		setContentView(glView);
		
		int one = 0x10000;
		setTitle(String.valueOf(one));
    }
}