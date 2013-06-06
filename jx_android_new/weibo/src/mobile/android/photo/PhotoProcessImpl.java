package mobile.android.photo;


import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

public abstract class PhotoProcessImpl implements PhotoProcess
{
	protected Bitmap mBitmap;
	protected Rect mRect;

	protected int mWidth; 
	protected int mHeight;


	
	
	protected Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			PhotoViewer.mpbPhotoProcess.setProgress(msg.what);
			super.handleMessage(msg);
		}

	};

	public PhotoProcessImpl(Bitmap bitmap)
	{
		mBitmap = bitmap;
	}
	public PhotoProcessImpl(Bitmap bitmap, Rect rect)
	{
		mBitmap = bitmap;
		mRect = rect;
	}


}
