package mobile.android.weibo;

import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.listener.impl.AuthDialogListenerImpl;
import mobile.android.weibo.net.PullFile;
import mobile.android.weibo.workqueue.WorkQueueMonitor;
import mobile.android.weibo.workqueue.WorkQueueStorage;
import mobile.android.weibo.workqueue.task.TaskMan;
import android.app.Activity;
import android.app.Application;

import com.weibo.net.Weibo;

public class GlobalObject extends Application implements Const
{
	private Weibo weibo;
	private int currentFace; // 当前显示哪个界面
	private int messageState; // 信息界面的状态，FACE_MESSAGE_AT FACE_MESSAGE_COMMENT
								// FACE_MESSAGE_FAVORITE

	private WorkQueueMonitor imageWorkQueueMonitor;
	private WorkQueueMonitor taskWorkQueueMonitor;
	private WorkQueueStorage workQueueStorage;

	public Weibo getWeibo(Activity activity)
	{
	
		if (weibo == null || !weibo.isSessionValid())
		{
			weibo = Weibo.getInstance();
			weibo.setupConsumerConfig(Consumer.consumerKey,
					Consumer.consumerSecret);
			weibo.setRedirectUrl(Consumer.redirectUrl);
			weibo.authorize(activity, new AuthDialogListenerImpl(activity));

		}

		return weibo;
	}

	public Weibo getWeibo()
	{
		return weibo;
	}

	public int getCurrentFace()
	{
		if (currentFace == 0)
			return FACE_HOME;
		else
			return currentFace;
	}

	public void setCurrentFace(int currentFace)
	{
		this.currentFace = currentFace;
	}

	public void setMessageState(int messageState)
	{
		this.messageState = messageState;
		this.currentFace = messageState;
	}

	public int getMessageState()
	{
		if (messageState == 0)
			return FACE_MESSAGE_AT;
		else
			return messageState;
	}

	public WorkQueueMonitor getImageWorkQueueMonitor(Activity activity)
	{
		if (imageWorkQueueMonitor == null)
		{

			imageWorkQueueMonitor = new WorkQueueMonitor(activity,
					getWorkQueueStorage(), new PullFile(), MONITOR_TYPE_IMAGE);
			imageWorkQueueMonitor.start();

		}
		return imageWorkQueueMonitor;
	}

	public WorkQueueMonitor getTaskWorkQueueMonitor(Activity activity)
	{
		if (taskWorkQueueMonitor == null)
		{

			taskWorkQueueMonitor = new WorkQueueMonitor(activity,
					getWorkQueueStorage(), new TaskMan(activity),
					MONITOR_TYPE_TASK);
			
			taskWorkQueueMonitor.start();

		}
		return taskWorkQueueMonitor;
	}

	public WorkQueueMonitor getImageWorkQueueMonitor()
	{
		return imageWorkQueueMonitor;
	}

	public WorkQueueMonitor getTaskWorkQueueMonitor()
	{
		return taskWorkQueueMonitor;
	}

	public WorkQueueStorage getWorkQueueStorage()
	{
		if (workQueueStorage == null)
		{
			workQueueStorage = new WorkQueueStorage();
		}
		return workQueueStorage;
	}

	public void closeWorkQueue()
	{
		if (imageWorkQueueMonitor != null)
		{
			imageWorkQueueMonitor.stop();
			imageWorkQueueMonitor = null;
		}
		if (taskWorkQueueMonitor != null)
		{
			taskWorkQueueMonitor.stop();
			taskWorkQueueMonitor = null;
		}
	}

}
