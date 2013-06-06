package mobile.android.weibo.adapters;

import java.util.List;

import mobile.android.weibo.objects.Status;
import android.app.Activity;

public class WeiboListNoMoreAdapter extends WeiboListAdapter
{
	public WeiboListNoMoreAdapter(Activity activity, List<Status> statuses,
			int faceType)
	{
		super(activity, statuses, faceType);
	}

	@Override
	public int getCount()
	{

		return statuses.size() ;
	}

}
