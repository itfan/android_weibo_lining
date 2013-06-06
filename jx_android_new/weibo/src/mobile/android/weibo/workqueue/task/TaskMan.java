package mobile.android.weibo.workqueue.task;

import java.util.List;

import mobile.android.weibo.net.PullFile;
import mobile.android.weibo.workqueue.DoingAndProcess;
import mobile.android.weibo.workqueue.task.process.CommentWeiboProcess;
import mobile.android.weibo.workqueue.task.process.FavoriteWeiboProcess;
import mobile.android.weibo.workqueue.task.process.PostWeiboProcess;
import mobile.android.weibo.workqueue.task.process.RepostWeiboProcess;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class TaskMan implements DoingAndProcess
{
	private Activity activity;
	private PostWeiboProcess postWeiboProcess;
	private RepostWeiboProcess repostWeiboProcess;
	private CommentWeiboProcess commentWeiboProcess;
	private FavoriteWeiboProcess favoriteWeiboProcess;
	private Handler msgHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			String str = String.valueOf(msg.obj);
			Toast.makeText(activity, str, Toast.LENGTH_LONG).show();
			super.handleMessage(msg);
		}
   
	};

	public TaskMan(Activity activity)
	{
		this.activity = activity;
		postWeiboProcess = new PostWeiboProcess(activity);
		repostWeiboProcess = new RepostWeiboProcess(activity);
		commentWeiboProcess = new CommentWeiboProcess(activity);
		favoriteWeiboProcess = new FavoriteWeiboProcess(activity);
	}

	@Override
	public void doingProcess(List list) throws Exception
	{
		for (Object task : list)
		{
			if (task instanceof PostWeiboTask)
			{
				try
				{
					postWeiboProcess.process((PostWeiboTask) task);
					Message msg = new Message();
					msg.obj = "成功发布微博";
					msgHandler.sendMessage(msg);
				}
				catch (Exception e)
				{
					exceptionProcess(task);
					throw e;
				}

			}
			else if (task instanceof PullFileTask)
			{
				PullFileTask pullFileTask = (PullFileTask) task;
				PullFile pullFile = new PullFile();
				pullFile.doingProcess(pullFileTask.fileUrl);
			}
			else if (task instanceof RepostWeiboTask)
			{
				try
				{
					repostWeiboProcess.process((RepostWeiboTask) task);
					Message msg = new Message();
					msg.obj = "成功转发微博";
					msgHandler.sendMessage(msg);
				}
				catch (Exception e)
				{
					exceptionProcess(task);
					throw e;
				}
			}
			else if (task instanceof CommentWeiboTask)
			{
				try
				{
					commentWeiboProcess.process((CommentWeiboTask) task);
					Message msg = new Message();
					msg.obj = "成功评论微博";
					msgHandler.sendMessage(msg);
				}
				catch (Exception e)
				{
					exceptionProcess(task);
					throw e;
				}
			}
			else if (task instanceof FavoriteWeiboTask)
			{
				try
				{
					favoriteWeiboProcess.process((FavoriteWeiboTask) task);
					
				}
				catch (Exception e)
				{
					exceptionProcess(task);
					throw e;
				}
			}
		}

	}

	private void exceptionProcess(Object task)
	{
		Message msg = new Message();
		// 任务处理
		if (task instanceof PostWeiboTask)
		{

			msg.obj = "微博发布失败";

		}
		else if (task instanceof RepostWeiboTask)
		{
			msg.obj = "转发微博失败";
		}
		else if (task instanceof CommentWeiboTask)
		{
			msg.obj = "评论微博失败";
		}
		else if (task instanceof FavoriteWeiboTask)
		{
			msg.obj = "收藏微博失败";
		}
		msgHandler.sendMessage(msg);
	}

}
