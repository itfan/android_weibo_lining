package mobile.android.jx.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class AnimView extends View
{
	private int value = 0;

	public AnimView(Context context)
	{
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (value == 300)
			value = 0;
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(value, 100, 20, paint);
		value++;
		invalidate();
	}

}
