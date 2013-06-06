package mobile.android.weibo;

import java.io.IOException;
import java.util.List;

import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboListNoMoreAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Status;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class WeiboListViewer extends Activity implements Const,
		RequestListener, OnClickListener, OnItemClickListener
{

	private List<Status> statuses;
	private WeiboListNoMoreAdapter statusListAdapter;
	private ListView statusList;
	private Button backButton;
	private TextView titleTextView;
	private String title;
	private int faceType;

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (statuses.size() == 0)
			{
				Toast.makeText(WeiboListViewer.this, "暂时没有微博",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				statusListAdapter = new WeiboListNoMoreAdapter(
						WeiboListViewer.this, statuses, 0);
				Tools.getGlobalObject(WeiboListViewer.this)
						.getImageWorkQueueMonitor(WeiboListViewer.this)
						.addDoneAndProcess(FACE_WEIBO_LIST, statusListAdapter);

				statusList.setAdapter(statusListAdapter);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibo_list_viewer);
		statusList = (ListView) findViewById(R.id.listview_statuses);
		statusList.setOnItemClickListener(this);
		backButton = (Button) findViewById(R.id.button_back);
		titleTextView = (TextView) findViewById(R.id.textview_title);
		statusListAdapter = (WeiboListNoMoreAdapter) getLastNonConfigurationInstance();
		title = getIntent().getStringExtra("title");
		faceType = getIntent().getIntExtra("face_type", FACE_HOT_STATUSES);
		if (title != null)
			titleTextView.setText(title);
		if (statusListAdapter == null)
		{
			if (faceType == FACE_HOT_STATUSES)
				WeiboManager.getHotStatusesAsync(this, this);
			else if (faceType == FACE_HOT_FAVORITIES)
				WeiboManager.getHotFavoritesAsync(this, this);
		}
		else
		{
			statusList.setAdapter(statusListAdapter);
		}

		backButton.setOnClickListener(this);

	}

	@Override
	public void onComplete(String response)
	{

		if (statusListAdapter == null)
		{

			statuses = (List<Status>) JSONAndObject.convert(Status.class,
					response, null);

			handler.sendEmptyMessage(0);
		}
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

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return statusListAdapter;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{

			case R.id.button_back:
				finish();
				break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{

		Status status = statusListAdapter.getStatus(position);
		if (status != null)
		{
			Intent intent = new Intent(this, WeiboViewer.class);

			WeiboViewer.status = status;
			startActivity(intent);
		}
	}

}
