package mobile.android.weibo.workqueue.task.process;

import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.workqueue.task.PostWeiboTask;
import android.app.Activity;

public class PostWeiboProcess
{
	private Activity activity;

	public PostWeiboProcess(Activity activity)
	{
		this.activity = activity;
	}

	// 提交微博
	public void process(PostWeiboTask task) throws Exception
	{
		WeiboManager.update(activity, Tools.getWeibo(activity), task.text,
				task.file);
	}
}
