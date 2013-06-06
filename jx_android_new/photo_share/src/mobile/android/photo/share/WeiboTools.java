package mobile.android.photo.share;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class WeiboTools
{

	public static String upload(Context context, Weibo weibo, String file,
			String status) throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());
		bundle.add("pic", file);
		bundle.add("status", status);

		String rlt = "";
		String url = Weibo.SERVER + "statuses/upload.json";
		try
		{
			rlt = weibo.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					weibo.getAccessToken());
		}
		catch (WeiboException e)
		{
			throw new WeiboException(e);
		}
		return rlt;
	}

	public static String update(Context context, Weibo weibo, String status)
			throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());

		bundle.add("status", status);

		String rlt = "";
		String url = Weibo.SERVER + "statuses/update.json";
		try
		{
			rlt = weibo.request(context, url, bundle, Utility.HTTPMETHOD_POST,
					weibo.getAccessToken());
		}
		catch (WeiboException e)
		{
			throw new WeiboException(e);
		}
		return rlt;
	}

	public static String scaleBitmap(String file)
	{
		long maxSize = 600 * 1024; // 600k
		File f = new File(file);
		if (f.exists())
		{
			int inSampleSize = (int) (f.length() / maxSize) ;
			if(f.length() % maxSize > maxSize / 2.0 )
			{
				inSampleSize++;
			}
		
			Options options = new Options();
			if (inSampleSize > 1)
			{
				String targetFile = "/sdcard/temp.jpg";

				try
				{

					options.inSampleSize = inSampleSize;
					Bitmap bitmap = BitmapFactory.decodeFile(file, options);
					FileOutputStream fos = new FileOutputStream(targetFile);
					bitmap.compress(CompressFormat.JPEG, 100, fos);
					fos.close();
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
				return targetFile;

			}
			else
			{
				return file;
			}
		}

		return null;
	}

}
