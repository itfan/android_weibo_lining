package mobile.android.weibo.listener.impl;

import java.io.IOException;
import java.util.List;

import mobile.android.weibo.R;
import mobile.android.weibo.WeiboMain;
import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboRefreshListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Favorite;
import mobile.android.weibo.objects.Status;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class StatusRequestListenerImpl implements RequestListener, Const
{

	private WeiboListAdapter adapter;
	private WeiboMain weiboMain;
	private View parent;
	private int faceType;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			adapter.putStatuses((List<Status>) msg.obj);
			adapter.hideMoreAnim();
			adapter.hideRefreshAnim();

			switch (faceType)
			{
				case FACE_HOME:
					if (weiboMain.homeList.getAdapter() == null)
					{
						weiboMain.homeList.setAdapter(adapter);
						weiboMain.data.imageWorkQueueMonitor.addDoneAndProcess(
								FACE_HOME, adapter);
					}
					View homeReloadAnim = parent
							.findViewById(R.id.progressbar_home_reload);
					View homeReload = parent
							.findViewById(R.id.button_home_reload);
					homeReloadAnim.setVisibility(View.GONE);
					homeReload.setVisibility(View.VISIBLE);
					break;
				case FACE_MESSAGE_AT:

					if (weiboMain.messageAtListView.getAdapter() == null)
					{
						weiboMain.messageAtListView.setAdapter(adapter);
						weiboMain.data.imageWorkQueueMonitor.addDoneAndProcess(
								faceType, adapter);
					}

					break;
				case FACE_MESSAGE_FAVORITE:

					weiboMain.messageFavoriteListView.setAdapter(adapter);
					weiboMain.data.imageWorkQueueMonitor.addDoneAndProcess(
							faceType, adapter);

					break;
				default:
					break;
			}

			super.handleMessage(msg);
		}

	};

	public StatusRequestListenerImpl(WeiboMain weiboMain, View parent,
			int faceType)
	{
		this.weiboMain = weiboMain;
		this.parent = parent;

		this.faceType = faceType;
	}

	@Override
	public void onComplete(String response)
	{

		List<Status> statuses = JSONAndObject.convert(Status.class, response,
				"statuses");

		switch (faceType)
		{
			case FACE_HOME:
				if (weiboMain.data.homeTimelineAdapter != null)
				{
					adapter = weiboMain.data.homeTimelineAdapter;
				}
				else
				{
					adapter = new WeiboListAdapter(weiboMain, null, faceType);
					weiboMain.data.homeTimelineAdapter = adapter;

				}

				break;
			case FACE_MESSAGE_AT:
				if (weiboMain.data.mentionAdapter != null)
				{
					adapter = weiboMain.data.mentionAdapter;
				}
				else
				{
					adapter = new WeiboRefreshListAdapter(weiboMain, null,
							faceType);
					weiboMain.data.mentionAdapter = (WeiboRefreshListAdapter) adapter;

				}
				break;
			case FACE_MESSAGE_FAVORITE:

				adapter = new WeiboRefreshListAdapter(weiboMain, null, faceType);
				weiboMain.data.favoriteAdapter = (WeiboRefreshListAdapter) adapter;
				List<Favorite> favorites = null;
				favorites = JSONAndObject.convert(Favorite.class, response,
						"favorites");
			
				statuses = WeiboManager.FavoriteToStatus(favorites);

				break;
			default:
				break;
		}

		Message msg = new Message();
		msg.obj = statuses;
		handler.sendMessage(msg);

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
