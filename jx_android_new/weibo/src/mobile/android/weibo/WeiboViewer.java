package mobile.android.weibo;

import java.io.File;
import java.io.IOException;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.workqueue.DoneAndProcess;
import mobile.android.weibo.workqueue.task.FavoriteWeiboTask;
import mobile.android.weibo.workqueue.task.ParentTask;
import mobile.android.weibo.workqueue.task.PullFileTask;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WeiboViewer extends Activity implements Const, RequestListener,
		DoneAndProcess, OnClickListener
{
	// 必须声明为static，使用intent传递status就不是原来的status了
	public static Status status;
	private ImageView profileImage;
	private ImageView verified;
	private TextView userName;
	private TextView statusText;
	private View insideContent;
	private TextView retweetdetailText;
	private ImageView retweetdetailImage;
	private ImageView statusImage;
	private TextView source;
	private Button forwardButton;
	private Button retweetdetailForwardButton;
	private Button commentButton;
	private Button retweetdetailCommentButton;
	private Button refreshButton;
	private View favorite;
	private View unfavorite;

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj instanceof PullFileTask)
			{
				String imageUrl = null;
				imageUrl = WeiboManager.getImageurl(WeiboViewer.this,
						status.user.profile_image_url, WeiboViewer.this);
				if (imageUrl != null)
				{
					profileImage.setImageURI(Uri.fromFile(new File(imageUrl)));
				}
				if (status.retweeted_status == null)
				{

					imageUrl = WeiboManager.getImageurl(WeiboViewer.this,
							status.bmiddle_pic, WeiboViewer.this);
					if (imageUrl != null)
					{
						statusImage.setImageURI(Uri
								.fromFile(new File(imageUrl)));
					}
				}
				else
				{
					imageUrl = WeiboManager.getImageurl(WeiboViewer.this,
							status.retweeted_status.bmiddle_pic,
							WeiboViewer.this);
					if (imageUrl != null)
					{
						retweetdetailImage.setImageURI(Uri.fromFile(new File(
								imageUrl)));
					}
				}
			}
			else if (msg.obj instanceof FavoriteWeiboTask)
			{
				if (favorite.getVisibility() == View.VISIBLE)
				{

					favorite.setVisibility(View.GONE);
					unfavorite.setVisibility(View.VISIBLE);
					status.favorited = true;
					Toast.makeText(WeiboViewer.this, "收藏成功", Toast.LENGTH_LONG)
							.show();

				}
				else
				{
					status.favorited = false;
					favorite.setVisibility(View.VISIBLE);
					unfavorite.setVisibility(View.GONE);
					Toast.makeText(WeiboViewer.this, "取消收藏成功",
							Toast.LENGTH_LONG).show();
				}
			}
			else if (msg.obj instanceof Status) // 刷新
			{
				loadContent();
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void doneProcess(ParentTask task)
	{
		Message msg = new Message();
		msg.obj = task;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		switch (view.getId())
		{
			case R.id.imageview_status_image:
			case R.id.imageview_retweetdetail_image:
				String url = status.original_pic;
				if (status.retweeted_status != null)
				{
					url = status.retweeted_status.original_pic;
				}
				intent = new Intent(this, PictureViewer.class);
				intent.putExtra("file_url", url);
				intent.putExtra("type", PICTURE_VIEWER_WEIBO_BROWSER);
				startActivity(intent);
				break;
			case R.id.button_forward:
			case R.id.linearlayout_forward:
				forward();
				break;
			case R.id.button_retweetdetail_forward:
				retweetdetailForward();
				break;
			case R.id.linearlayout_comment:
				comment();
				break;
			case R.id.linearlayout_fav:
			case R.id.linearlayout_unfav:
				favorite(!status.favorited);
				break;
			case R.id.button_comment:
				intent = new Intent(this, CommentListViewer.class);
				intent.putExtra("status_id", status.id);
				intent.putExtra("text", "//@" + status.user.name + ":"
						+ status.text);
				startActivity(intent);
				break;
			case R.id.button_retweetdetail_comment:
				intent = new Intent(this, CommentListViewer.class);
				intent.putExtra("status_id", status.retweeted_status.id);
				intent.putExtra("text", "//@"
						+ status.retweeted_status.user.name + ":"
						+ status.retweeted_status.text);
				startActivity(intent);
				break;
			case R.id.button_back:
				finish();
				break;
			case R.id.button_refresh:
				WeiboManager.getStatusAsync(this, status.id, this);
				break;

			default:
				break;
		}
	}

	private void favorite(boolean fav)
	{
		FavoriteWeiboTask favoriteWeiboTask = new FavoriteWeiboTask();
		favoriteWeiboTask.id = status.id;
		favoriteWeiboTask.fav = fav;
		favoriteWeiboTask.doneAndProcess = this;
		Tools.getGlobalObject(this).getWorkQueueStorage()
				.addTask(favoriteWeiboTask);
	}

	private void forward()
	{
		String text = "//@" + status.user.name + ":" + status.text;
		Intent intent = new Intent(this, PostWeibo.class);
		intent.putExtra("type", TYPE_FORWARD);
		intent.putExtra("title", "转发微博");
		intent.putExtra("text", text);
		intent.putExtra("status_id", status.id);
		startActivity(intent);
	}

	private void comment()
	{
		
		Intent intent = new Intent(this, PostWeibo.class);
		intent.putExtra("type", TYPE_COMMENT);
		intent.putExtra("title", "评论微博");
		
		intent.putExtra("status_id", status.id);
		startActivity(intent);
	}

	private void retweetdetailForward()
	{
		String text = "//@" + status.retweeted_status.user.name + ":"
				+ status.retweeted_status.text;
		Intent intent = new Intent(this, PostWeibo.class);
		intent.putExtra("type", TYPE_FORWARD);
		intent.putExtra("title", "转发微博");
		intent.putExtra("text", text);
		intent.putExtra("status_id", status.retweeted_status.id);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_viewer);
		profileImage = (ImageView) findViewById(R.id.imageview_profile_image);
		verified = (ImageView) findViewById(R.id.imageview_verified);
		userName = (TextView) findViewById(R.id.textview_name);
		statusText = (TextView) findViewById(R.id.textview_text);
		statusImage = (ImageView) findViewById(R.id.imageview_status_image);
		insideContent = findViewById(R.id.linearlayout_inside_content);
		retweetdetailText = (TextView) findViewById(R.id.textview_retweetdetail_text);
		retweetdetailImage = (ImageView) findViewById(R.id.imageview_retweetdetail_image);
		source = (TextView) findViewById(R.id.textview_source);
		forwardButton = (Button) findViewById(R.id.button_forward);
		commentButton = (Button) findViewById(R.id.button_comment);
		retweetdetailForwardButton = (Button) findViewById(R.id.button_retweetdetail_forward);
		retweetdetailCommentButton = (Button) findViewById(R.id.button_retweetdetail_comment);
		favorite = findViewById(R.id.linearlayout_fav);
		unfavorite = findViewById(R.id.linearlayout_unfav);
		refreshButton = (Button)findViewById(R.id.button_refresh);
		findViewById(R.id.linearlayout_forward).setOnClickListener(this);
		findViewById(R.id.linearlayout_comment).setOnClickListener(this);
		findViewById(R.id.button_back).setOnClickListener(this);
		refreshButton.setOnClickListener(this);

		retweetdetailImage.setOnClickListener(this);
		statusImage.setOnClickListener(this);
		forwardButton.setOnClickListener(this);
		commentButton.setOnClickListener(this);
		retweetdetailForwardButton.setOnClickListener(this);
		retweetdetailCommentButton.setOnClickListener(this);
		favorite.setOnClickListener(this);
		unfavorite.setOnClickListener(this);
		loadContent();
	}

	private void loadContent()
	{
		String imageUrl = WeiboManager.getImageurl(this,
				status.user.profile_image_url, this);
		if (imageUrl != null)
		{
			profileImage.setImageURI(Uri.fromFile(new File(imageUrl)));
		}

		userName.setText(status.user.name);
		Tools.userVerified(verified, status.user.verified_type);
		statusText.setText(Tools.changeTextToFace(this,
				Html.fromHtml(Tools.atBlue(status.text))));
		source.setText("来自  " + status.getTextSource());

		if (status.retweeted_status == null)
		{
			insideContent.setVisibility(View.GONE);
		}
		else
		{
			insideContent.setVisibility(View.VISIBLE);
			retweetdetailText.setText(Html.fromHtml(Tools.atBlue("@"
					+ status.retweeted_status.user.name + ":" + status.retweeted_status.text)));
			retweetdetailForwardButton.setText(String
					.valueOf(status.retweeted_status.reposts_count));
			retweetdetailCommentButton.setText(String
					.valueOf(status.retweeted_status.comments_count));
		}
		if (WeiboManager.hasPicture(status))
		{

			if (status.retweeted_status == null)
			{
				statusImage.setVisibility(View.VISIBLE);
				retweetdetailImage.setVisibility(View.GONE);

				imageUrl = WeiboManager.getImageurl(this, status.bmiddle_pic,
						this);
				if (imageUrl != null)
				{
					statusImage.setImageURI(Uri.fromFile(new File(imageUrl)));
				}
			}
			else
			{
				statusImage.setVisibility(View.GONE);

				retweetdetailImage.setVisibility(View.VISIBLE);

				imageUrl = WeiboManager.getImageurl(this,
						status.retweeted_status.bmiddle_pic, this);
				if (imageUrl != null)
				{
					retweetdetailImage.setImageURI(Uri.fromFile(new File(
							imageUrl)));
				}

			}

		}
		forwardButton.setText(String.valueOf(status.reposts_count));
		commentButton.setText(String.valueOf(status.comments_count));
		if (status.favorited)
		{
			favorite.setVisibility(View.GONE);
			unfavorite.setVisibility(View.VISIBLE);

		}
		else
		{
			favorite.setVisibility(View.VISIBLE);
			unfavorite.setVisibility(View.GONE);
		}
	}

	@Override
	public void onComplete(String response)
	{

		JSONAndObject.convertSingleObject(status, response);
		Message msg = new Message();
		msg.obj = status;
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
