package mobile.android.jx.surfaceview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback
{
	private SurfaceHolder holder;

	public MySurfaceView(Context context)
	{
		super(context);
		holder = this.getHolder();// 获取holder
		holder.addCallback(this);

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		Canvas canvas = holder.lockCanvas(null);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.WHITE);

		canvas.drawRect(new RectF(40, 60, 80, 80), mPaint);
		holder.unlockCanvasAndPost(canvas);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// TODO Auto-generated method stub

	}

}
