package mobile.android.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class ImageViewExt extends ImageView
{
	public Bitmap mBitmap;
	public Rect mDstRect;

	public ImageViewExt(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		/*if (mBitmap != null)
		{
			setImageBitmap(mBitmap);
			
			canvas.drawBitmap(mBitmap, null, mDstRect, null);
			Log.d("ondraw","ondraw");
			mBitmap = null;
		}*/
		
		super.onDraw(canvas);
	}

}
