package mobile.android.jx.draw2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class MyView extends View
{
	private final float[] mVerts = new float[10];

	public MyView(Context context)
	{
		super(context);
		int w = 200, h = 100;

		setXY(mVerts, 0, w / 2, h / 2);
		setXY(mVerts, 1, 0, 0);
		setXY(mVerts, 2, w, 0);

	}

	private static void setXY(float[] array, int index, float x, float y)
	{
		array[index * 2 + 0] = x;
		array[index * 2 + 1] = y;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		// 绘制点
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(6);

		canvas.drawPoint(50, 12, paint);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(12);
		canvas.drawPoint(100, 20, paint);

		paint.setColor(Color.BLUE);
		paint.setStrokeWidth(8);
		canvas.drawPoints(new float[]
		{ 150, 22, 200, 20 }, paint);
		canvas.drawPoints(new float[]
		{ 260, 22, 280, 20 }, 2, 2, paint);

		// 绘制直线
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(4);
		canvas.drawLine(20, 40, 160, 40, paint);

		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(2);

		canvas.drawLines(new float[]
		{ 30, 60, 200, 90, 30, 90, 200, 60 }, paint);

		paint.setColor(Color.YELLOW);
		canvas.drawLines(new float[]
		{ 30, 100, 300, 100, 36, 20, 120, 30 }, 0, 4, paint);

		// 绘制三角形
		canvas.save();
		canvas.translate(20, 150);
		canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 6, new float[]
		{ 100, 50, 0, 0, 200, 0 }, 0, null, 0, null, 0, null, 0, 0, paint);
		canvas.restore();

		// 绘制矩形和菱形
		paint.setStyle(Style.FILL);
		canvas.drawRect(120, 210, 180, 255, paint);
		paint.setStyle(Style.STROKE);
		canvas.drawRect(new Rect(20, 210, 100, 255), paint);

		canvas.save();
		canvas.translate(20, 260);
		canvas.drawVertices(Canvas.VertexMode.TRIANGLE_STRIP, 8, new float[]
		{ 70, 0, 10, 50, 130, 50, 70, 100 }, 0, null, 0, null, 0, null, 0, 0,
				paint);
		canvas.restore();

		canvas.save();
		canvas.translate(160, 260);
		canvas.drawVertices(Canvas.VertexMode.TRIANGLE_FAN, 8, new float[]
		{ 70, 0, 10, 50, 130, 50, 70, 100 }, 0, null, 0, null, 0, null, 0, 0,
				paint);
		canvas.restore();

		// 绘制圆、弧和椭圆

		canvas.drawCircle(200, 400, 40, paint);

		canvas.drawRect(new Rect(10, 370, 70, 410), paint);
		canvas.drawArc(new RectF(10, 370, 70, 410), 30, 180, false, paint);
		canvas.drawArc(new RectF(100, 320, 160, 470), 30, 180, true, paint);
		
		canvas.drawArc(new RectF(200, 180, 300, 250), 0, 360, false, paint);

		
		//  绘制文本
		paint.setColor(Color.WHITE);
		paint.setTextSize(40);
		paint.setStyle(Style.FILL);
	
		canvas.drawText("文字", 240, 380, paint);
		paint.setStyle(Style.STROKE);
		
		canvas.drawText("文字", 240, 440, paint);
	}
}
