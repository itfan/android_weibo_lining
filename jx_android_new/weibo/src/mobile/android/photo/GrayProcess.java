package mobile.android.photo;


import android.graphics.Bitmap;
import android.graphics.Color;

public class GrayProcess extends PhotoProcessImpl
{

	public GrayProcess(Bitmap bitmap)
	{ 
		super(bitmap);
	}

	@Override
	public void work()
	{
		for (int i = 0; i < mBitmap.getWidth(); i++)
		{
			for (int j = 0; j < mBitmap.getHeight(); j++)
			{
				int red = Color.red(mBitmap.getPixel(i, j));
				int green = Color.green(mBitmap.getPixel(i, j));
				int blue = Color.blue(mBitmap.getPixel(i, j));

				// �Ҷ��㷨
				int gray = (int) ((red & 0xff) * 0.3);
				gray += (int) ((green & 0xff) * 0.59);
				gray += (int) ((blue & 0xff) * 0.11);

				// �ɺ죬�̣���ֵ�õ��Ҷ�ֵ��
				mBitmap.setPixel(i, j, (255 << 24) | (gray << 16) | (gray << 8)
						| gray);

			}
			mHandler.sendEmptyMessage(i);
		}

	}

}
