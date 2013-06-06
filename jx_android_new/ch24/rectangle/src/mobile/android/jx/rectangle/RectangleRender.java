package mobile.android.jx.rectangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

public class RectangleRender implements Renderer
{

	int one = 0x10000;

	private IntBuffer rectangleBuffer;
//	private int[] rectangleVertices = new int[]
	//{ one, one, 0, -one, one, 0, one, -one, 0, -one, -one, 0 };
	private int[] rectangleVertices = new int[]
           { one, one, 0, one, -one, 0, one * 2, one, 0, one * 2, -one, 0 };
    private float angle = 0;
	@Override
	public void onDrawFrame(GL10 gl)
	{

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glLoadIdentity();
		//gl.glPushMatrix();
		gl.glTranslatef(0.0f, 0.0f, -4.0f);
		
		//gl.glLoadIdentity();
	//	gl.glPopMatrix();
		gl.glRotatef(angle++, 0.0f, 1.0f, 0.0f);

		gl.glVertexPointer(3, GL10.GL_FIXED, 0, rectangleBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		float ratio = (float) width / height;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);

		gl.glLoadIdentity();

		gl.glFrustumf(-ratio *2, ratio * 2, -2, 2, 1, 10);
		

		gl.glMatrixMode(GL10.GL_MODELVIEW);

		

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(rectangleVertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		rectangleBuffer = byteBuffer.asIntBuffer();
		rectangleBuffer.put(rectangleVertices);
		rectangleBuffer.position(0);
	}

}
