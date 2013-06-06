package mobile.android.jx.rotate.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class MyView extends View
{
	private Bitmap bitmap;

	public MyView(Context context)
	{
		super(context);
		bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.android);
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		canvas.drawBitmap(bitmap,
				new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
				new Rect(50, 20,( bitmap.getWidth()/2) + 50,
						(bitmap.getHeight() /2)+ 20), null);
		
		
		Matrix matrix = new Matrix();
		matrix.setRotate(45, 160,240);
		canvas.setMatrix(matrix);
		canvas.drawBitmap(bitmap, 350, 260,null);

	}
}
