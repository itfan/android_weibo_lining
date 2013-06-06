package mobile.android.weibo.listener.impl;

import java.io.IOException;
import java.util.List;

import mobile.android.weibo.WeiboMain;
import mobile.android.weibo.adapters.CommentListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.JSONAndObject;
import mobile.android.weibo.objects.Comment;
import android.os.Handler;
import android.os.Message;

import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.WeiboException;

public class CommentRequestListenerImpl implements RequestListener, Const
{

	private CommentListAdapter adapter;
	private WeiboMain weiboMain;
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			if (weiboMain.messageCommentListView.getAdapter() == null)
			{
				weiboMain.messageCommentListView.setAdapter(adapter);
	
			}

			super.handleMessage(msg);
		}

	};

	public CommentRequestListenerImpl(WeiboMain weiboMain)
	{
		this.weiboMain = weiboMain;

	}

	@Override
	public void onComplete(String response)
	{

		List<Comment> comments = JSONAndObject.convert(Comment.class, response,
				"comments");

		if (weiboMain.data.commentAdapter != null)
		{
			adapter = weiboMain.data.commentAdapter;
		}
		else
		{
			adapter = new CommentListAdapter(weiboMain, comments);
			weiboMain.data.commentAdapter = adapter;

		}

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
