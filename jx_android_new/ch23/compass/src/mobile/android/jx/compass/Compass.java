package mobile.android.jx.compass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class Compass extends Activity
{

	private SensorManager sensorManager;
	private Sensor sensor;
	private CompassView view;
	private float[] values;

	private final SensorEventListener mListener = new SensorEventListener()
	{
		public void onSensorChanged(SensorEvent event)
		{

			values = event.values;
			if (view != null)
			{
				view.invalidate();
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{
		}
	};

	@Override
	protected void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		view = new CompassView(this);
		setContentView(view);
	}

	@Override
	protected void onResume()
	{

		super.onResume();

		sensorManager.registerListener(mListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	protected void onStop()
	{

		sensorManager.unregisterListener(mListener);
		super.onStop();
	}

	private class CompassView extends View
	{

		private Path path = new Path();

		public CompassView(Context context)
		{
			super(context);
			path.moveTo(0, -50);
			path.lineTo(-20, 60);
			path.lineTo(0, 50);
			path.lineTo(20, 60);

			path.close();
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			Paint paint = new Paint();

			canvas.drawColor(Color.WHITE);
			paint.setColor(Color.BLACK);

			int w = canvas.getWidth();
			int h = canvas.getHeight();
			int cx = w / 2;
			int cy = h / 2;

			canvas.translate(cx, cy);
			if (values != null)
			{
				canvas.rotate(-values[0]);
			}
			canvas.drawPath(path, paint);
		}

	}
}
