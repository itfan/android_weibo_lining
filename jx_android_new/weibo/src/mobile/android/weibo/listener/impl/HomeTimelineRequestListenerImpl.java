package mobile.android.weibo.listener.impl;

import java.io.IOException;
import java.util.List;

import mobile.android.weibo.WeiboMain;
import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboRefreshListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.objects.Status;

import android.os.Handler;
import android.os.Message;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class HomeTimelineRequestListenerImpl implements RequestListener, Const
{
	private WeiboMain weiboMain;
	private WeiboListAdapter homeAdapter;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			weiboMain.homeList.setAdapter(homeAdapter);
			weiboMain.data.homeTimelineAdapter = homeAdapter;
			super.handleMessage(msg);
		}

	};

	public HomeTimelineRequestListenerImpl(WeiboMain weiboMain)
	{
		this.weiboMain = weiboMain;
	}

	@Override
	public void onComplete(String response)
	{
		List<Status> statuses = JSONAndObject.convert(Status.class, response,
				"statuses");
		homeAdapter = new WeiboRefreshListAdapter(weiboMain, statuses,
				FACE_MESSAGE_AT);
		weiboMain.data.imageWorkQueueMonitor.addDoneAndProcess(FACE_HOME,
				homeAdapter);
		handler.sendEmptyMessage(0);

	}

	@Override
	public void onIOException(IOException e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException e)
	{
		// TODO Auto-generated method stub

	}

}
