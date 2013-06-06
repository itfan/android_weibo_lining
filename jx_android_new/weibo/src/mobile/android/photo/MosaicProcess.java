package mobile.android.photo;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;

public class MosaicProcess extends PhotoProcessImpl 
{
	protected List<Rect> mRegions = new ArrayList<Rect>();
	protected int mRegionWidth;
	protected int mRegionHeight;
	
	public MosaicProcess(Bitmap bitmap, Rect rect)
	{ 
		super(bitmap, rect);

	}

	private Handler mhandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			PhotoViewer.mpbPhotoProcess.setProgress(msg.arg1);
			PhotoViewer.mpbPhotoProcess.setMax(msg.arg2);

			super.handleMessage(msg);
		}

	};
	private void splitRegions()
	{
		Rect rect = null;
		
		int left = 0, top = 0;
		mRegions.clear();
		do
		{
			do
			{
				rect = new Rect();
				rect.left = left;
				rect.right = rect.left + mRegionWidth;

				if (rect.right >= mWidth)
					rect.right = mWidth - 1;
				rect.top = top;
				rect.bottom = rect.top + mRegionHeight;
				if (rect.bottom >= mHeight)
					rect.bottom = mHeight - 1;
				left = rect.right;
				if(mRect != null)
				{
					rect.left = rect.left + mRect.left;
					rect.top = rect.top + mRect.top;
					rect.right = rect.right + mRect.left;
					rect.bottom = rect.bottom + mRect.top;
				}
				
				mRegions.add(rect);

				
			}
			while (left < mWidth - 1);

			left = 0;
			top = top + mRegionHeight;
		}
		while (top < mHeight);
	}

	@Override
	public void work()
	{
		mWidth = mRect.right - mRect.left + 1;
		mHeight = mRect.bottom - mRect.top + 1;

		mRegionWidth = ProcessBitmapRegions.MOSAIC_SINGLE_REGION_SIZE;
		mRegionHeight = ProcessBitmapRegions.MOSAIC_SINGLE_REGION_SIZE;
		splitRegions();

		for (int k = 0; k < mRegions.size(); k++)
		{
			Rect rect = mRegions.get(k);
			int color = mBitmap.getPixel((rect.left + rect.right) / 2,
					(rect.top + rect.bottom) / 2);
			for (int i = rect.left; i <= rect.right; i++)
			{
				for (int j = rect.top; j <= rect.bottom; j++)
				{
					if (i < mBitmap.getWidth() && j < mBitmap.getHeight())
						mBitmap.setPixel(i, j, color);
				}
			}
			Message message = new Message();
			message.arg2 = mRegions.size();
			message.arg1 = k + 1;
			mhandler.sendMessage(message);

		}

	}

}
