package mobile.android.weibo.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile.android.weibo.R;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.workqueue.DoneAndProcess;
import mobile.android.weibo.workqueue.task.ParentTask;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeiboListAdapter extends BaseAdapter implements DoneAndProcess,
		Const
{

	protected Activity activity;
	protected int faceType;
	protected LayoutInflater layoutInflater;
	protected List<Status> statuses;

	public WeiboListAdapter(Activity activity)
	{
		this.activity = activity;

	}

	@Override
	public void doneProcess(ParentTask task)
	{

		notifyDataSetChanged();

	}

	public WeiboListAdapter(Activity activity, List<Status> statuses, int faceType)
	{
		this.activity = activity;
		this.faceType = faceType;
		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.statuses = new ArrayList<Status>();
		if (statuses != null)
			this.statuses.addAll(statuses);
		try
		{
			StorageManager.saveList(statuses, PATH_STORAGE, faceType);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	@Override
	public int getCount()
	{

		return statuses.size() + 1;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Status getStatus(int position)
	{
		if (statuses.size() > 0)
		{
			return statuses.get(position);
		}
		else
		{
			return null;
		}
	}

	public long getMinId()
	{
		if (statuses.size() > 0)
			return statuses.get(statuses.size() - 1).id;
		else
			return Long.MAX_VALUE;
	}

	public long getMaxId()
	{
		if (statuses.size() > 0)
			return statuses.get(0).id;
		else
			return 0;
	}

	public void putStatuses(List<Status> statuses)
	{
		if (statuses == null || this.statuses == null)
			return;
		if (statuses.size() == 0)
			return;
		if (this.statuses.size() == 0)
		{
			this.statuses.addAll(statuses);

		}
		else if (statuses
				.get(0)
				.getCreatedAt()
				.before(this.statuses.get(this.statuses.size() - 1)
						.getCreatedAt()))
		{
			this.statuses.addAll(statuses);

		}
		// 添加的statuses比原来的新，并且数量小于等于默认返回数量，直接将statuses添加到前面
		else if (statuses.get(statuses.size() - 1).getCreatedAt()
				.after(this.statuses.get(0).getCreatedAt())
				&& statuses.size() <= DEFAULT_STATUS_COUNT)
		{
			this.statuses.addAll(0, statuses);

		}
		else
		// 其他情况
		{

			this.statuses.clear();
			this.statuses.addAll(statuses);
		}
		try
		{
			StorageManager.saveList(this.statuses, PATH_STORAGE,
					faceType);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		this.notifyDataSetChanged();
	}

	// 控制View行为的
	private boolean showMoreAnimFlag = false;
	protected boolean showRefreshAnimFlag = false;

	public void showMoreAnim()
	{
		showMoreAnimFlag = true;
		notifyDataSetChanged();
	}

	public void hideMoreAnim()
	{
		showMoreAnimFlag = false;
		notifyDataSetChanged();
	}
	public void showRefreshAnim()
	{
		showRefreshAnimFlag = true;
		notifyDataSetChanged();
	}
	public void hideRefreshAnim()
	{
		showRefreshAnimFlag = false;
		notifyDataSetChanged();
	}

	// 通过url装载要显示的图像，如果图像文件不存在，回通过hideView标志决定是否隐藏ImageView组件
	// hideView: true 隐藏ImageView hideView：false 不做任何动作
	private void loadImage(ImageView imageView, String url, boolean hideView)
	{

		if (url != null)
		{
			String imageUrl = WeiboManager.getImageurl(activity, url);

			if (imageUrl != null)
			{
				imageView.setImageURI(Uri.fromFile(new File(imageUrl)));
				imageView.setVisibility(View.VISIBLE);
				return;
			}

		}
		if (hideView)
			imageView.setVisibility(View.GONE);
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
		refreshWeiboListItem.setVisibility(View.GONE);
		if (position < statuses.size())
		{
			weiboListItem.setVisibility(View.VISIBLE);

			moreWeiboListItem.setVisibility(View.GONE);
			Status status = statuses.get(position);

			TextView statusText = (TextView) convertView
					.findViewById(R.id.textview_text);
			TextView name = (TextView) convertView
					.findViewById(R.id.textview_name);
			TextView createdAt = (TextView) convertView
					.findViewById(R.id.textview_created_at);
			ImageView profileImage = (ImageView) convertView
					.findViewById(R.id.imageview_profile_image);
			profileImage.setImageResource(R.drawable.portrait);
			ImageView picture = (ImageView) convertView
					.findViewById(R.id.imageview_picture);
			ImageView statusImage = (ImageView) convertView
					.findViewById(R.id.imageview_status_image);

			ImageView verified = (ImageView) convertView
					.findViewById(R.id.imageview_verified);

			verified.setVisibility(View.GONE);
			
			if (status.user != null)
			{
				Tools.userVerified(verified, status.user.verified_type);
			}

			statusImage.setImageBitmap(null);
			LinearLayout insideContent = (LinearLayout) convertView
					.findViewById(R.id.linearlayout_inside_content);
			ImageView retweetdetailImage = (ImageView) convertView
					.findViewById(R.id.imageview_retweetdetail_image);
			retweetdetailImage.setImageBitmap(null);
			TextView retweetdetailText = (TextView) convertView
					.findViewById(R.id.textview_retweetdetail_text);
			TextView source = (TextView) convertView
					.findViewById(R.id.textview_source);

			// 装载图像
			if (status.user != null)
				loadImage(profileImage, status.user.profile_image_url, false);
			loadImage(statusImage, status.thumbnail_pic, true);

			// ///////////////////

			statusText.setText(Tools.changeTextToFace(activity,
					Html.fromHtml(Tools.atBlue(status.text))));
			if (status.user != null)
				name.setText(status.user.name);
			// 当前微博有图像
			if (WeiboManager.hasPicture(status))
				picture.setVisibility(View.VISIBLE);
			else
				picture.setVisibility(View.INVISIBLE);
			createdAt.setText(Tools.getTimeStr(status.getCreatedAt(),
					new Date()));
			source.setText("来自  " + status.getTextSource());

			if (status.retweeted_status != null
					&& status.retweeted_status.user != null)
			{
				insideContent.setVisibility(View.VISIBLE);

				retweetdetailText.setText(Html.fromHtml(Tools.atBlue("@"
						+ status.retweeted_status.user.name + ":"
						+ status.retweeted_status.text)));
				loadImage(retweetdetailImage,
						status.retweeted_status.thumbnail_pic, true);
			}
			else
			{
				insideContent.setVisibility(View.GONE);
			}
		}
		else
		{
			weiboListItem.setVisibility(View.GONE);
			moreWeiboListItem.setVisibility(View.VISIBLE);
			View moreAnim = convertView.findViewById(R.id.progressbar_more);
			
			if (showMoreAnimFlag)
			{
				moreAnim.setVisibility(View.VISIBLE);
			}
			else
			{
				moreAnim.setVisibility(View.GONE);
			}
		}
		return convertView;
	}
}
