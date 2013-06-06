package mobile.android.weibo.library;

import java.util.List;

import mobile.android.weibo.WeiboMain.WeiboMainData;
import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboRefreshListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.objects.Status;
import android.app.Activity;
import android.widget.ListView;

public class WeiboData implements Const
{
	public static WeiboListAdapter loadWeiboListData(Activity activity,
			int type, ListView listView)
	{
		return loadWeiboListData(activity, type, listView, null);
	}

	public static WeiboListAdapter loadWeiboListData(Activity activity,
			int type, ListView listView, List<Status> statuses)
	{
		WeiboListAdapter adapter = null;
		if (Tools.hasWeibo(activity))
		{

			WeiboMainData weiboMainData = (WeiboMainData) activity
					.getLastNonConfigurationInstance();
			if (weiboMainData == null)
			{

				switch (type)
				{
					case FACE_HOME:
						if (statuses == null)
							statuses = WeiboManager.getHomeTimeline(activity);
						break;
					
					default:
						break;
				}
				switch (type)
				{
					case FACE_HOME:
						if (statuses == null)
							statuses = WeiboManager.getHomeTimeline(activity);
						adapter = new WeiboListAdapter(activity, statuses, type);
						break;
					case FACE_MESSAGE_AT:
						if (statuses == null)
							statuses = WeiboManager.getMentions(activity);
						adapter = new WeiboRefreshListAdapter(activity,
								statuses, type);
						break;
					case FACE_MESSAGE_FAVORITE:
						if (statuses == null)
							statuses = WeiboManager.getFavorites(activity);
						adapter = new WeiboRefreshListAdapter(activity,
								statuses, type);
						break;
						
				}

			}
			else
			{
				switch (type)
				{
					case FACE_HOME:
						adapter = weiboMainData.homeTimelineAdapter;
						break;
					case FACE_MESSAGE_AT:
						adapter = weiboMainData.mentionAdapter;
						break;
					case FACE_MESSAGE_FAVORITE:
						adapter =weiboMainData.favoriteAdapter;
					default:
						break;
				}

			}

			listView.setAdapter(adapter);

		}
		return adapter;
	}
}
