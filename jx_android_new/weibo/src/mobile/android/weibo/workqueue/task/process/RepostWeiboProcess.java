package mobile.android.weibo.workqueue.task.process;

import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.workqueue.task.PostWeiboTask;
import mobile.android.weibo.workqueue.task.RepostWeiboTask;
import android.app.Activity;

public class RepostWeiboProcess
{
	private Activity activity;

	public RepostWeiboProcess(Activity activity)
	{
		this.activity = activity;
	}

	// 提交微博
	public void process(RepostWeiboTask task) throws Exception
	{
		WeiboManager.repost(activity, Tools.getWeibo(activity), task.id,
				task.text, task.isComment);

	}
}
