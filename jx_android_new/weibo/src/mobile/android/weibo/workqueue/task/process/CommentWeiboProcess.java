package mobile.android.weibo.workqueue.task.process;

import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.workqueue.task.CommentWeiboTask;
import android.app.Activity;

public class CommentWeiboProcess
{
	private Activity activity;

	public CommentWeiboProcess(Activity activity)
	{
		this.activity = activity;
	}

	// 提交微博
	public void process(CommentWeiboTask task) throws Exception
	{
		WeiboManager.comment(activity, Tools.getWeibo(activity), task.id,
				task.text, task.commentOri);
		if (task.postWeibo)
		{
			WeiboManager.repost(activity, Tools.getWeibo(activity), task.id,
					task.weiboText, 0);
		}
	}
}
