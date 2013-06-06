package mobile.android.weibo;

import java.io.IOException;
import java.util.List;

import mobile.android.weibo.adapters.CommentListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Comment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class CommentListViewer extends Activity implements Const,
		RequestListener, OnClickListener
{

	private List<Comment> comments;
	private CommentListAdapter commentListAdapter;
	private ListView commentList;
	private Button commentButton;
	private TextView titleTextView;
	private Button backButton;
	private long statusId;
	private String text;
	private String title;

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			if (comments.size() == 0)
			{
				Toast.makeText(CommentListViewer.this, "暂时没有评论",
						Toast.LENGTH_LONG).show();
			}
			else
			{
				commentListAdapter = new CommentListAdapter(
						CommentListViewer.this, comments);
				commentList.setAdapter(commentListAdapter);
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_list_viewer);
		commentList = (ListView) findViewById(R.id.listview_comment);
		commentButton = (Button) findViewById(R.id.button_comment);
		backButton = (Button) findViewById(R.id.button_back);
		titleTextView = (TextView) findViewById(R.id.textview_title);
		statusId = getIntent().getLongExtra("status_id", 0);
		text = getIntent().getStringExtra("text");
		title = getIntent().getStringExtra("title");
		if (title != null)
			titleTextView.setText(title);
		commentListAdapter = (CommentListAdapter) getLastNonConfigurationInstance();
		if (statusId != 0)
		{

			if (commentListAdapter == null)
			{
				WeiboManager.getCommentsAsync(this, statusId, this);
			}
			else
			{
				commentList.setAdapter(commentListAdapter);
			}
		}
		else
		{

			if (commentListAdapter == null)
			{
				WeiboManager.getHotCommentsAsync(this, this);
			}
			else
			{
				commentList.setAdapter(commentListAdapter);
			}
			commentButton.setVisibility(View.INVISIBLE);
		}
		backButton.setOnClickListener(this);
		commentButton.setOnClickListener(this);
	}

	@Override
	public void onComplete(String response)
	{

		if (commentListAdapter == null)
		{
			if (statusId != 0)
			{
				comments = (List<Comment>) JSONAndObject.convert(Comment.class,
						response, "comments");
			}
			else
			{
				comments = (List<Comment>) JSONAndObject.convert(Comment.class,
						response, null);
			}
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
		return commentListAdapter;
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.button_comment:
				comment();
				break;
			case R.id.button_back:
				finish();
				break;
		}
	}

	private void comment()
	{

		Intent intent = new Intent(this, PostWeibo.class);
		intent.putExtra("type", TYPE_COMMENT);
		intent.putExtra("title", "评论微博");
		intent.putExtra("text", text);
		intent.putExtra("status_id", statusId);
		startActivity(intent);
	}
}
