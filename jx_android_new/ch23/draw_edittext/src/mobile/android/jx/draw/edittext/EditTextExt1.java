package mobile.android.jx.draw.edittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextExt1 extends EditText
{

	public EditTextExt1(Context context, AttributeSet attrs)
	{
		super(context, attrs);

	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		Paint paint = new Paint();
		
		paint.setTextSize(18);
		paint.setColor(Color.GRAY);
		canvas.drawText("请输入姓名:", 2, getHeight() / 2 + 5,
				paint);

		super.onDraw(canvas);
	}
}
