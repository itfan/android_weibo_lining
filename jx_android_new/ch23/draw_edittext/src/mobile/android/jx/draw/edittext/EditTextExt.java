package mobile.android.jx.draw.edittext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextExt extends EditText
{
	private Bitmap bitmap;

	public EditTextExt(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.star);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		canvas.drawBitmap(bitmap, 5, (getHeight() - bitmap.getHeight()) / 2,
				new Paint());

		super.onDraw(canvas);
	}
}
