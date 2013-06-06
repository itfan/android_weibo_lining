package mobile.android.weibo.adapters;

import java.util.List;

import mobile.android.weibo.R;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.workqueue.task.ParentTask;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeiboRefreshListAdapter extends WeiboListAdapter
{

	public WeiboRefreshListAdapter(Activity activity, List<Status> statuses,
			int faceType)
	{
		super(activity, statuses, faceType);
	}

	@Override
	public int getCount()
	{
		return super.getCount() + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = layoutInflater
					.inflate(R.layout.weibo_list_item, null);
		}
		View weiboListItem = convertView
				.findViewById(R.id.linearlayout_weibo_list_item);
		View moreWeiboListItem = convertView
				.findViewById(R.id.relativelayout_more_weibo_list_item);
		View refreshWeiboListItem = convertView
				.findViewById(R.id.relativelayout_refresh_weibo_list_item);
		if (position == 0)
		{
			weiboListItem.setVisibility(View.GONE);
			moreWeiboListItem.setVisibility(View.GONE);
			refreshWeiboListItem.setVisibility(View.VISIBLE);
            View refreshAnim = convertView.findViewById(R.id.progressbar_refresh);
			
			if (showRefreshAnimFlag)
			{
				refreshAnim.setVisibility(View.VISIBLE);
			}
			else
			{
				refreshAnim.setVisibility(View.GONE);
			}
			return convertView;
		}
		else
		{
			View view = super.getView(position - 1, convertView, parent);
			if(faceType == FACE_MESSAGE_FAVORITE)
			{
				TextView more = (TextView)view.findViewById(R.id.textview_more);
				more.setText("刷新");
			}
	
			return view;
		}
	}

}
