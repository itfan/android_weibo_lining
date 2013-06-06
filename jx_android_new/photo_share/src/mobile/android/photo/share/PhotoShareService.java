package mobile.android.photo.share;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

import com.weibo.net.Weibo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.SimpleAdapter;

public class PhotoShareService extends Service implements Runnable
{
	private String path = android.os.Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/DCIM/Camera";
	private long maxTime;
	private Thread thread;
	private boolean flag = true;

	@Override
	public IBinder onBind(Intent intent)
	{

		return null;
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		
		if (thread == null)
		{
			maxTime = new Date().getTime();
			flag = true;
			thread = new Thread(this);
			thread.start();
		}

	}

	@Override
	public void onDestroy()
	{
		flag = false;
		thread = null;
		super.onDestroy();
	}

	@Override
	public void run()
	{
		while (flag)
		{
			try
			{
				Thread.sleep(500);
			}
			catch (Exception e)
			{

			}
			File file = new File(path);
			File[] files = file.listFiles(new FileFilter()
			{

				@Override
				public boolean accept(File pathname)
				{
					if (pathname.lastModified() > maxTime)
						return true;
					else
						return false;
				} 
			});
			
			if (files != null && files.length > 0)
			{
				try
				{
					maxTime = files[0].lastModified();
					String filename = WeiboTools.scaleBitmap(files[0]
							.getAbsolutePath());
					if (filename != null)
						WeiboTools.upload(this, PhotoShare.weibo, filename,
								"分享图片");

				}
				catch (Exception e)
				{

				}
			}

		}

	}

}
