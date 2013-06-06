package mobile.android.jx.rectangle;


import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class Rectangle extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new GLSurfaceView(this);
		RectangleRender triangleRender = new RectangleRender();
		glView.setRenderer(triangleRender);
		setContentView(glView);
    }
}