package mobile.android.photo;


import android.graphics.Bitmap;
import android.graphics.Rect;

public class PhotoThread implements Runnable
{
	private Bitmap mBitmap;
	private Rect mRect;
	private PhotoProcess mPhotoProcess;

	public PhotoThread(PhotoProcess photoProcess)
	{
		mPhotoProcess = photoProcess;
	}


	@Override
	public void run()
	{
		mPhotoProcess.work();
		ProcessBitmapRegions.mAllThreadEnd.onFinish();

		

	}

}
