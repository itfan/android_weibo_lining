package mobile.android.jx.triangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;

public class TriangleRender implements Renderer
{

	private FloatBuffer triangleBuffer;
	private float[] triangleVertices = new float[]
	{ 0, 1.0f, 0, -1.0f, -1.0f, 0, 1.0f, -1.0f, 0 };
	int one = 0x10000;
	private int[] triangleVertices1 = new int[]
	{ 0, one, 0,0, -one, -one, 0, one,one, -one, 0 };
	private IntBuffer triangleBuffer1;

	@Override
	public void onDrawFrame(GL10 gl)
	{

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glLoadIdentity();
	//	GLU.gluLookAt(gl, 0, 0, 1, 0f, 0f, 0f, 0f, 0.0f, 0.0f);
		gl.glTranslatef(0f, 0.0f, -5.00001f);
// stride的解释：http://www.gamedev.net/topic/431342-glvertexpointer-stride-parameter/
//  http://www.cnblogs.com/melode11/archive/2011/06/13/2079705.html
//  一个顶点到另一个顶点开始字节的字节数
		//  可将颜色与顶点放在一起在
		gl.glVertexPointer(3, GL10.GL_FIXED,16, triangleBuffer1);

		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		float ratio = (float) height / width;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);

		gl.glFrustumf(-1, 1, -ratio, ratio, 1, 10);

		gl.glMatrixMode(GL10.GL_MODELVIEW);

	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		ByteBuffer byteBuffer = ByteBuffer
				.allocateDirect(triangleVertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());

		triangleBuffer = byteBuffer.asFloatBuffer();
		triangleBuffer.put(triangleVertices);
		triangleBuffer.position(0);

		byteBuffer = ByteBuffer.allocateDirect(triangleVertices1.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		triangleBuffer1 = byteBuffer.asIntBuffer();
		triangleBuffer1.put(triangleVertices1);
		triangleBuffer1.position(0);

	}

}
