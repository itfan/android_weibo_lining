package mobile.android.weibo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mobile.android.weibo.adapters.CommentListAdapter;
import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboRefreshListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboData;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.listener.impl.CommentRequestListenerImpl;
import mobile.android.weibo.listener.impl.StatusRequestListenerImpl;
import mobile.android.weibo.objects.Comment;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.objects.User;
import mobile.android.weibo.workqueue.DoneAndProcess;
import mobile.android.weibo.workqueue.WorkQueueMonitor;
import mobile.android.weibo.workqueue.task.FavoriteWeiboTask;
import mobile.android.weibo.workqueue.task.ParentTask;
import mobile.android.weibo.workqueue.task.PullFileTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class WeiboMain extends Activity implements Const, OnTouchListener,
		OnItemClickListener, OnClickListener, OnMenuItemClickListener,
		OnItemLongClickListener, DoneAndProcess
{
	public WeiboMainData data;
	public ListView homeList;

	private LinearLayout linearLayoutHome;
	private LinearLayout linearLayoutMessage;
	private LinearLayout linearLayoutSelfInfo;
	private LinearLayout linearLayoutSquare;
	public ListView messageAtListView;
	public ListView messageCommentListView;
	public ListView messageFavoriteListView;
	public TextView username;
	private Button messageAtButton;
	private Button messageCommentButton;
	private Button messageFavoriteButton;

	private ImageView selfinfoProfileImage;
	private TextView selfinfoAddress;
	private TextView selfinfoVerifiedReason;
	private TextView selfinfoFriendsCount;
	private TextView selfinfoFollowersCount;
	private TextView selfinfoStatusesCount;
	private TextView selfinfoName;
	private ImageView selfinfoVerified;

	private ListView squareListView;

	private int position;
	// public int timelineType = TYPE_HOME_TIMELINE;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.obj instanceof FavoriteWeiboTask)
			{
				FavoriteWeiboTask favoriteWeiboTask = (FavoriteWeiboTask) msg.obj;
				if (favoriteWeiboTask.fav)
				{
					Toast.makeText(WeiboMain.this, "收藏微博成功", Toast.LENGTH_LONG)
							.show();
				}
				else
				{
					Toast.makeText(WeiboMain.this, "取消收藏成功", Toast.LENGTH_LONG)
							.show();
				}
			}
			else if (msg.what == HANDLER_TYPE_LOAD_PROFILE_IMAGE)
			{
				String url = (String) msg.obj;
				String imageUrl = WeiboManager.getImageurl(WeiboMain.this, url,
						WeiboMain.this);
				if (imageUrl != null)
				{
					selfinfoProfileImage.setImageURI(Uri.fromFile(new File(
							imageUrl)));
				}
			}
			else if (msg.obj instanceof User)
			{
				User user = (User) msg.obj;
				selfinfoAddress.setText(user.location);
				selfinfoVerifiedReason.setText(user.verified_reason);
				selfinfoFriendsCount
						.setText(String.valueOf(user.friends_count));
				selfinfoFollowersCount.setText(String
						.valueOf(user.followers_count));
				selfinfoStatusesCount.setText(String
						.valueOf(user.statuses_count));
				selfinfoName.setText(user.name);
				Tools.userVerified(selfinfoVerified, user.verified_type);
				String imageUrl = WeiboManager.getImageurl(WeiboMain.this,
						user.profile_image_url, WeiboMain.this);

				if (imageUrl != null)
				{
					selfinfoProfileImage.setImageURI(Uri.fromFile(new File(
							imageUrl)));
				}

			}
			super.handleMessage(msg);
		}

	};

	public static class WeiboMainData
	{
		public WeiboListAdapter homeTimelineAdapter;
		public WeiboRefreshListAdapter mentionAdapter;
		public WeiboRefreshListAdapter favoriteAdapter;
		public CommentListAdapter commentAdapter;
		public ArrayAdapter<String> squareAdapter;

		public WorkQueueMonitor imageWorkQueueMonitor;
		public WorkQueueMonitor taskWorkQueueMonitor;
		public User user;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (Consumer.verify(this))
		{
			Tools.getWeibo(this);

	
			View mainView = loadMainLayout();
			setContentView(mainView);

			loadLinearLayout(mainView);
			loadView();
			getWindow().setBackgroundDrawable(null);

			Tools.updateFullscreenStatus(this, false);
			data = (WeiboMainData) getLastNonConfigurationInstance();
			if (data == null)
			{
				data = new WeiboMainData();
				if (!(Tools.getWeibo(this) == null || !Tools.getWeibo(this)
						.isSessionValid()))
				{
					int faceType = Tools.getGlobalObject(this).getCurrentFace();

					List<Status> statuses = StorageManager.loadList(this,
							faceType);

					data.imageWorkQueueMonitor = Tools.getGlobalObject(this)
							.getImageWorkQueueMonitor(this);
					data.taskWorkQueueMonitor = Tools.getGlobalObject(this)
							.getTaskWorkQueueMonitor(this);
					switch (faceType)
					{
						case FACE_HOME:
							if (statuses == null)
							{
								statuses = WeiboManager.getHomeTimeline(this);
							}
							data.homeTimelineAdapter = WeiboData
									.loadWeiboListData(this, faceType,
											homeList, statuses);
							data.imageWorkQueueMonitor.addDoneAndProcess(
									FACE_MESSAGE_AT, data.homeTimelineAdapter);
							break;
						case FACE_MESSAGE_AT:
							if (statuses == null)
							{

								statuses = WeiboManager.getMentions(this);
							}
							data.mentionAdapter = (WeiboRefreshListAdapter) WeiboData
									.loadWeiboListData(this, faceType,
											messageAtListView, statuses);
							data.imageWorkQueueMonitor.addDoneAndProcess(
									FACE_MESSAGE_AT, data.mentionAdapter);
							break;
						case FACE_MESSAGE_COMMENT:
							List<Comment> comments = StorageManager.loadList(
									this, faceType);
							if (comments == null)
							{
								comments = WeiboManager
										.getCommentTimeline(this);
							}

							data.commentAdapter = new CommentListAdapter(this,
									comments);
							messageCommentListView
									.setAdapter(data.commentAdapter);
							break;
						case FACE_MESSAGE_FAVORITE:
							if (statuses == null)
							{
								statuses = WeiboManager.getFavorites(this);
							}
							data.favoriteAdapter = (WeiboRefreshListAdapter) WeiboData
									.loadWeiboListData(this, faceType,
											messageFavoriteListView, statuses);
							data.imageWorkQueueMonitor
									.addDoneAndProcess(FACE_MESSAGE_FAVORITE,
											data.favoriteAdapter);

							break;
						case FACE_SELFINFO:
							selfinfoRefresh();
							break;
						case  FACE_SQUARE:
							
							loadSquare();
							break;
						default:
							break;
					}

				}
			}
			else
			{
				homeList.setAdapter(data.homeTimelineAdapter);
				messageAtListView.setAdapter(data.mentionAdapter);
				messageCommentListView.setAdapter(data.commentAdapter);
				messageFavoriteListView.setAdapter(data.favoriteAdapter);
				if (data != null)
				{
					Message msg = new Message();

					msg.obj = data.user;
					handler.sendMessage(msg);
				}
				loadSquare();
			}
		}

	}

	// event method

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return data;
	}

	@Override
	public void doneProcess(ParentTask task)
	{
		Message msg = new Message();
		msg.obj = task;
		if (task instanceof PullFileTask)
		{
			msg.what = HANDLER_TYPE_LOAD_PROFILE_IMAGE;
			msg.obj = ((PullFileTask) task).fileUrl;
		}
		handler.sendMessage(msg);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id)
	{
		this.position = position;
		return false;
	}

	@Override
	public void onClick(View view)
	{
		Intent intent = null;
		GlobalObject globalObject = Tools.getGlobalObject(this);
		switch (view.getId())
		{
			case R.id.button_home_reload:

				long sinceId = data.homeTimelineAdapter.getMaxId() + 1;
				WeiboManager.getHomeTimeline(this, sinceId, 0,
						DEFAULT_STATUS_COUNT, true,
						new StatusRequestListenerImpl(this, linearLayoutHome,
								FACE_HOME));
				View homeReloadAnim = linearLayoutHome
						.findViewById(R.id.progressbar_home_reload);
				View homeReload = linearLayoutHome
						.findViewById(R.id.button_home_reload);
				homeReloadAnim.setVisibility(View.VISIBLE);
				homeReload.setVisibility(View.GONE);
				break;
			case R.id.button_selfinfo_reload:
				selfinfoRefresh();
				break;
			case R.id.button_home_post_weibo:
			case R.id.button_selfinfo_post_weibo:
				intent = new Intent(this, PostWeibo.class);
				startActivity(intent);
				break;
			case R.id.button_message_at:

				globalObject.setMessageState(FACE_MESSAGE_AT);

				refreshMessageFace(globalObject);
				if (data.mentionAdapter == null)
				{
					WeiboManager.getMentionsAsync(this,
							new StatusRequestListenerImpl(this,
									linearLayoutMessage, FACE_MESSAGE_AT));
				}
				else
				{
					if (messageAtListView.getAdapter() == null)
						messageAtListView.setAdapter(data.mentionAdapter);
				}
				break;
			case R.id.button_message_comment:

				globalObject.setMessageState(FACE_MESSAGE_COMMENT);

				refreshMessageFace(globalObject);
				if (data.commentAdapter == null)
				{
					WeiboManager.getCommentTimelineAsync(this,
							new CommentRequestListenerImpl(this));

				}
				else
				{
					if (messageCommentListView.getAdapter() == null)
						messageCommentListView.setAdapter(data.commentAdapter);
				}
				break;
			case R.id.button_message_favorite:

				globalObject.setMessageState(FACE_MESSAGE_FAVORITE);
				refreshMessageFace(globalObject);

				if (data.favoriteAdapter == null)
				{
					WeiboManager
							.getFavoritesAsync(this,
									new StatusRequestListenerImpl(this,
											linearLayoutMessage,
											FACE_MESSAGE_FAVORITE));
				}
				else
				{
					if (messageFavoriteListView.getAdapter() == null)
						messageFavoriteListView
								.setAdapter(data.favoriteAdapter);
				}
				break;
			default:
				break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		Intent intent = null;
		switch (parent.getId())
		{
			case R.id.listview_home:

				// 按更多项
				if (position == data.homeTimelineAdapter.getCount() - 1)
				{
					long maxId = data.homeTimelineAdapter.getMinId() - 1;
					WeiboManager.getHomeTimeline(this, 0, maxId,
							DEFAULT_STATUS_COUNT, true,
							new StatusRequestListenerImpl(this,
									linearLayoutHome, FACE_HOME));
					data.homeTimelineAdapter.showMoreAnim();
				}
				else
				{
					Status status = data.homeTimelineAdapter
							.getStatus(position);
					if (status != null)
					{
						intent = new Intent(this, WeiboViewer.class);

						WeiboViewer.status = status;
						startActivity(intent);
					}
				}
				break;
			case R.id.listview_message_at:
				if (position == data.mentionAdapter.getCount() - 1)
				{

					long maxId = data.mentionAdapter.getMinId() - 1;
					WeiboManager.getMentions(this, 0, maxId,
							DEFAULT_STATUS_COUNT, true,
							new StatusRequestListenerImpl(this,
									linearLayoutMessage, FACE_MESSAGE_AT));
					data.mentionAdapter.showMoreAnim();

				}
				else if (position == 0)
				{
					long sinceId = data.mentionAdapter.getMaxId() + 1;
					WeiboManager.getMentions(this, sinceId, 0,
							DEFAULT_STATUS_COUNT, true,
							new StatusRequestListenerImpl(this,
									linearLayoutMessage, FACE_MESSAGE_AT));
					data.mentionAdapter.showRefreshAnim();
				}
				else
				{
					Status status = data.mentionAdapter.getStatus(position - 1);
					if (status != null)
					{
						intent = new Intent(this, WeiboViewer.class);
						// intent.putExtra("status", status);
						WeiboViewer.status = status;
						startActivity(intent);
					}
				}
				break;
			case R.id.listview_message_favorite:
				if (position == data.favoriteAdapter.getCount() - 1)
				{

					WeiboManager
							.getFavorites(this, DEFAULT_STATUS_COUNT, true,
									new StatusRequestListenerImpl(this,
											linearLayoutMessage,
											FACE_MESSAGE_FAVORITE));
					data.favoriteAdapter.showMoreAnim();

				}
				else if (position == 0)
				{

					WeiboManager
							.getFavorites(this, 100, true,
									new StatusRequestListenerImpl(this,
											linearLayoutMessage,
											FACE_MESSAGE_FAVORITE));
					data.favoriteAdapter.showRefreshAnim();
				}
				else
				{
					Status status = data.favoriteAdapter
							.getStatus(position - 1);
					if (status != null)
					{
						intent = new Intent(this, WeiboViewer.class);

						WeiboViewer.status = status;
						startActivity(intent);
					}
				}
				break;
			case R.id.listview_square:
				switch (position)
				{
					case 0: // 热门微博
						intent = new Intent(this, WeiboListViewer.class);
						intent.putExtra("title", "热门微博");
						intent.putExtra("face_type", FACE_HOT_STATUSES);
						startActivity(intent);
						break;
					case 1: // 热门收藏
						intent = new Intent(this, WeiboListViewer.class);
						intent.putExtra("title", "热门收藏");
						intent.putExtra("face_type", FACE_HOT_FAVORITIES);
						startActivity(intent);
						break;
					case 2: // 热门评论
						intent = new Intent(this, CommentListViewer.class);
						intent.putExtra("title", "热门评论");
						startActivity(intent);
						break;

				}
				break;
			default:
				break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == Consumer.CONSUMER_REQUEST_CODE)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{

			GlobalObject globalObject = Tools.getGlobalObject(this);
			switch (view.getId())
			{
				case R.id.button_home:
					hideLinearLayout();
					linearLayoutHome.setVisibility(View.VISIBLE);
					globalObject.setCurrentFace(FACE_HOME);
					if (data.homeTimelineAdapter == null)
					{
						WeiboManager.getHomeTimelineAsync(this,
								new StatusRequestListenerImpl(this,
										linearLayoutHome, FACE_HOME));
					}

					else
					{
						if (homeList.getAdapter() == null)
							homeList.setAdapter(data.homeTimelineAdapter);
					}
					break;
				case R.id.button_message:
					hideLinearLayout();
					linearLayoutMessage.setVisibility(View.VISIBLE);
					globalObject.setCurrentFace(globalObject.getMessageState());
					refreshMessageFace(globalObject);
					switch (globalObject.getMessageState())
					{
						case FACE_MESSAGE_AT:
							if (data.mentionAdapter == null)
							{
								WeiboManager.getMentionsAsync(this,
										new StatusRequestListenerImpl(this,
												linearLayoutMessage,
												FACE_MESSAGE_AT));
							}
							else
							{
								if (messageAtListView.getAdapter() == null)
									messageAtListView
											.setAdapter(data.mentionAdapter);
							}
							break;
						case FACE_MESSAGE_COMMENT:
							if (data.commentAdapter == null)
							{
								WeiboManager.getCommentTimelineAsync(this,
										new CommentRequestListenerImpl(this));

							}
							else
							{
								if (messageCommentListView.getAdapter() == null)
									messageCommentListView
											.setAdapter(data.commentAdapter);
							}
							break;
						case FACE_MESSAGE_FAVORITE:
							if (data.favoriteAdapter == null)
							{
								WeiboManager.getFavoritesAsync(this,
										new StatusRequestListenerImpl(this,
												linearLayoutMessage,
												FACE_MESSAGE_FAVORITE));
							}
							else
							{
								if (messageFavoriteListView.getAdapter() == null)
									messageFavoriteListView
											.setAdapter(data.favoriteAdapter);
							}
							break;
						default:
							break;
					}

					break;
				case R.id.button_selfinfo:
					hideLinearLayout();
					linearLayoutSelfInfo.setVisibility(View.VISIBLE);
					globalObject.setCurrentFace(FACE_SELFINFO);
					selfinfoRefresh();
					break;
				case R.id.button_square:
					hideLinearLayout();
					linearLayoutSquare.setVisibility(View.VISIBLE);
					globalObject.setCurrentFace(FACE_SQUARE);
					loadSquare();

					break;
				case R.id.button_more:
					new AlertDialog.Builder(this)
							.setMessage("新浪微博Android客户端\n\n作者：李宁")
							.setPositiveButton("关闭", null).show();
					break;

			}
		}
		return false;
	}

	// private method

	private void loadSquare()
	{
		if (data != null && data.squareAdapter != null)
		{
			squareListView.setAdapter(data.squareAdapter);
		}
		else
		{
			ArrayAdapter<String> squareAdapter = new ArrayAdapter<String>(this,
					R.layout.simple_list_item_1, android.R.id.text1,
					new String[]
					{ "热门微博", "热门收藏", "热门评论" });
			squareListView.setAdapter(squareAdapter);
			if (data != null)
				data.squareAdapter = squareAdapter;
		}
	}

	private void selfinfoRefresh()
	{
		WeiboManager.getUserAsync(this,
				StorageManager.getValue(this, "uid", 0), new RequestListener()
				{

					@Override
					public void onComplete(String response)
					{
						User user = new User();
						JSONAndObject.convertSingleObject((Object) user,
								response);
						if (data != null)
							data.user = user;
						Message msg = new Message();
						msg.obj = user;
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
				});
	}

	private View loadMainLayout()
	{
		View mainView = getLayoutInflater().inflate(R.layout.main, null);
		LinearLayout linearLayout = (LinearLayout) mainView
				.findViewById(R.id.linearlayout_toolbar);

		String[] toolbarButtonTexts = getResources().getStringArray(
				R.array.array_toolbar_btn_text);
		TypedArray toolbarButtonImageResourceIds = getResources()
				.obtainTypedArray(R.array.array_toolbar_btn_image_resource);

		TypedArray toolbarButtonIds = getResources().obtainTypedArray(
				R.array.array_toolbar_btn_id);
		GlobalObject globalObject = Tools.getGlobalObject(this);
		for (int i = 0; i < toolbarButtonTexts.length; i++)
		{
			View view = getLayoutInflater().inflate(R.layout.toolbar_btn, null);
			view.setId(toolbarButtonIds.getResourceId(i, 0));

			view.setOnTouchListener(this);
			ImageView imageview = (ImageView) view
					.findViewById(R.id.imageview_toolbar_btn);

			TextView textView = (TextView) view
					.findViewById(R.id.textview_toolbar_text);
			imageview.setImageDrawable(toolbarButtonImageResourceIds
					.getDrawable(i));
			textView.setText(toolbarButtonTexts[i]);
			view.setFocusableInTouchMode(true);
			switch (globalObject.getCurrentFace())
			{
				case FACE_HOME:
					if (toolbarButtonIds.getResourceId(i, 0) == R.id.button_home)
						view.requestFocus();
					break;
				case FACE_MESSAGE_AT:
				case FACE_MESSAGE_COMMENT:
				case FACE_MESSAGE_FAVORITE:
					if (toolbarButtonIds.getResourceId(i, 0) == R.id.button_message)
						view.requestFocus();
					break;
				case FACE_SELFINFO:
					if (toolbarButtonIds.getResourceId(i, 0) == R.id.button_selfinfo)
						view.requestFocus();
					break;
				case FACE_SQUARE:
					if (toolbarButtonIds.getResourceId(i, 0) == R.id.button_square)
						;
					view.requestFocus();
					break;
				default:
					break;
			}
			linearLayout.addView(view, new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, 1));

		}

		return mainView;
	}

	private void hideLinearLayout()
	{
		linearLayoutHome.setVisibility(View.INVISIBLE);
		linearLayoutMessage.setVisibility(View.INVISIBLE);
		linearLayoutSelfInfo.setVisibility(View.INVISIBLE);
		linearLayoutSquare.setVisibility(View.INVISIBLE);
	}

	private void resetMessageFace()
	{
		messageAtListView.setVisibility(View.GONE);
		messageCommentListView.setVisibility(View.GONE);
		messageFavoriteListView.setVisibility(View.GONE);
		messageAtButton.setTextColor(Color.BLACK);
		messageCommentButton.setTextColor(Color.BLACK);
		messageFavoriteButton.setTextColor(Color.BLACK);
	}

	private void refreshMessageFace(GlobalObject globalObject)
	{

		resetMessageFace();
		switch (globalObject.getMessageState())
		{
			case FACE_MESSAGE_AT:
				messageAtListView.setVisibility(View.VISIBLE);
				messageAtButton.setTextColor(Color.RED);
				break;
			case FACE_MESSAGE_COMMENT:
				messageCommentListView.setVisibility(View.VISIBLE);
				messageCommentButton.setTextColor(Color.RED);
				break;
			case FACE_MESSAGE_FAVORITE:
				messageFavoriteListView.setVisibility(View.VISIBLE);
				messageFavoriteButton.setTextColor(Color.RED);
				break;
			default:
				break;
		}
	}

	// 装载主布局中LinearLayout
	private void loadLinearLayout(View mainView)
	{
		linearLayoutHome = (LinearLayout) mainView
				.findViewById(R.id.linearlayout_home);
		linearLayoutMessage = (LinearLayout) mainView
				.findViewById(R.id.linearlayout_message);
		linearLayoutSelfInfo = (LinearLayout) mainView
				.findViewById(R.id.linearlayout_selfinfo);
		linearLayoutSquare = (LinearLayout) mainView
				.findViewById(R.id.linearlayout_square);
		hideLinearLayout();
		switch (Tools.getGlobalObject(this).getCurrentFace())
		{
			case FACE_HOME:
				linearLayoutHome.setVisibility(View.VISIBLE);
				break;

			case FACE_MESSAGE_AT:
			case FACE_MESSAGE_COMMENT:
			case FACE_MESSAGE_FAVORITE:
				linearLayoutMessage.setVisibility(View.VISIBLE);
				break;

			case FACE_SELFINFO:
				linearLayoutSelfInfo.setVisibility(View.VISIBLE);
				break;
			case FACE_SQUARE:
				linearLayoutSquare.setVisibility(View.VISIBLE);
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo)
	{

		getMenuInflater().inflate(R.menu.weibo_context_menu, menu);
		MenuItem forwardMenu = menu.findItem(R.id.menu_forward);
		MenuItem commentMenu = menu.findItem(R.id.menu_comment);
		MenuItem favoriteMenu = menu.findItem(R.id.menu_favorite);
		MenuItem unfavoriteMenu = menu.findItem(R.id.menu_unfavorite);
		MenuItem oriForwardMenu = menu.findItem(R.id.menu_ori_forward);
		MenuItem selfinfoMenu = menu.findItem(R.id.menu_selfinfo);
		forwardMenu.setOnMenuItemClickListener(this);
		commentMenu.setOnMenuItemClickListener(this);
		favoriteMenu.setOnMenuItemClickListener(this);
		oriForwardMenu.setOnMenuItemClickListener(this);
		selfinfoMenu.setOnMenuItemClickListener(this);
		unfavoriteMenu.setOnMenuItemClickListener(this);
		Status status = status = data.homeTimelineAdapter.getStatus(position);
		if (status.retweeted_status == null)
			oriForwardMenu.setVisible(false);
		if (status.favorited)
			favoriteMenu.setVisible(false);
		else
			unfavoriteMenu.setVisible(false);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void finish()
	{
		Tools.getGlobalObject(this).closeWorkQueue();

		super.finish();
	}

	private void forward(Status status)
	{
		String text = "//@" + status.user.name + ":" + status.text;
		Intent intent = new Intent(this, PostWeibo.class);
		intent.putExtra("type", TYPE_FORWARD);
		intent.putExtra("title", "转发微博");
		intent.putExtra("text", text);
		intent.putExtra("status_id", status.id);
		startActivity(intent);
	}

	private void favorite(boolean fav)
	{
		Status status = data.homeTimelineAdapter.getStatus(position);
		FavoriteWeiboTask favoriteWeiboTask = new FavoriteWeiboTask();
		favoriteWeiboTask.id = status.id;
		favoriteWeiboTask.fav = fav;
		favoriteWeiboTask.status = status;
		favoriteWeiboTask.doneAndProcess = this;
		Tools.getGlobalObject(this).getWorkQueueStorage()
				.addTask(favoriteWeiboTask);
	}

	@Override
	public boolean onMenuItemClick(MenuItem item)
	{

		Status status = null;
		Intent intent = null;
		switch (item.getItemId())
		{
			case R.id.menu_forward:
				status = data.homeTimelineAdapter.getStatus(position);
				forward(status);
				break;
			case R.id.menu_ori_forward:
				status = data.homeTimelineAdapter.getStatus(position).retweeted_status;
				forward(status);
				break;
			case R.id.menu_comment:
				status = data.homeTimelineAdapter.getStatus(position);
				intent = new Intent(this, PostWeibo.class);
				intent.putExtra("type", TYPE_COMMENT);
				intent.putExtra("title", "评论微博");
				intent.putExtra("status_id", status.id);
				startActivity(intent);
				break;
			case R.id.menu_favorite:
				favorite(true);
				break;
			case R.id.menu_unfavorite:
				favorite(false);
				break;
			default:
				break;
		}
		return true;
	}

	private void loadView()
	{
		GlobalObject globalObject = Tools.getGlobalObject(this);
		homeList = (ListView) linearLayoutHome.findViewById(R.id.listview_home);
		username = (TextView) linearLayoutHome
				.findViewById(R.id.textview_home_name);

		long uid = StorageManager.getValue(this, "uid", 0);
		if (uid > 0)
		{
			User user = WeiboManager.getUser(this, uid);
			if (user != null)
			{

				username.setText(user.name);

			}
		}

		registerForContextMenu(homeList);
		homeList.setOnItemClickListener(this);
		homeList.setOnItemLongClickListener(this);
		View homeReload = linearLayoutHome
				.findViewById(R.id.button_home_reload);
		View homePostWeibo = linearLayoutHome
				.findViewById(R.id.button_home_post_weibo);
		homeReload.setOnClickListener(this);
		homePostWeibo.setOnClickListener(this);

		// message

		messageAtButton = (Button) linearLayoutMessage
				.findViewById(R.id.button_message_at);
		messageCommentButton = (Button) linearLayoutMessage
				.findViewById(R.id.button_message_comment);
		messageFavoriteButton = (Button) linearLayoutMessage
				.findViewById(R.id.button_message_favorite);
		messageAtListView = (ListView) linearLayoutMessage
				.findViewById(R.id.listview_message_at);
		messageCommentListView = (ListView) linearLayoutMessage
				.findViewById(R.id.listview_message_comment);
		messageFavoriteListView = (ListView) linearLayoutMessage
				.findViewById(R.id.listview_message_favorite);

		messageAtButton.setOnClickListener(this);
		messageCommentButton.setOnClickListener(this);
		messageFavoriteButton.setOnClickListener(this);
		messageAtListView.setOnItemClickListener(this);
		messageAtListView.setOnItemLongClickListener(this);
		messageFavoriteListView.setOnItemClickListener(this);
		messageFavoriteListView.setOnItemLongClickListener(this);

		refreshMessageFace(globalObject);

		// selfinfo
		selfinfoProfileImage = (ImageView) linearLayoutSelfInfo
				.findViewById(R.id.imageview_profile_image);
		selfinfoAddress = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_address);
		selfinfoVerifiedReason = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_verified_reason);
		selfinfoFriendsCount = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_friends_count);
		selfinfoFollowersCount = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_followers_count);
		selfinfoStatusesCount = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_statuses_count);
		selfinfoName = (TextView) linearLayoutSelfInfo
				.findViewById(R.id.textview_name);
		selfinfoVerified = (ImageView) linearLayoutSelfInfo
				.findViewById(R.id.imageview_verified);
		linearLayoutSelfInfo.findViewById(R.id.button_selfinfo_post_weibo)
				.setOnClickListener(this);
		linearLayoutSelfInfo.findViewById(R.id.button_selfinfo_reload)
				.setOnClickListener(this);

		// square
		squareListView = (ListView) linearLayoutSquare
				.findViewById(R.id.listview_square);
		squareListView.setOnItemClickListener(this);

	}
}