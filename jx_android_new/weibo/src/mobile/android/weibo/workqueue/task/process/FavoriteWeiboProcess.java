package mobile.android.weibo.workqueue.task.process;

import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.workqueue.task.FavoriteWeiboTask;
import android.app.Activity;

public class FavoriteWeiboProcess
{
	private Activity activity;

	public FavoriteWeiboProcess(Activity activity)
	{
		this.activity = activity;
	}
	// 提交微博
	public void process(FavoriteWeiboTask task) throws Exception
	{
		WeiboManager.favorite(activity, Tools.getWeibo(activity), task.id,
				task.fav);
		if(task.status != null)
		{
			task.status.favorited = task.fav;
		}

	}
}
