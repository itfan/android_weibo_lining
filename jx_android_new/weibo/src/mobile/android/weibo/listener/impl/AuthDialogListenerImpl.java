package mobile.android.weibo.listener.impl;

import mobile.android.weibo.WeiboMain;
import mobile.android.weibo.adapters.WeiboListAdapter;
import mobile.android.weibo.adapters.WeiboRefreshListAdapter;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.library.WeiboData;
import mobile.android.weibo.library.WeiboManager;
import mobile.android.weibo.objects.User;
import android.app.Activity;
import android.os.Bundle;

import com.weibo.net.DialogError;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class AuthDialogListenerImpl implements WeiboDialogListener, Const
{
	private Activity activity;

	public AuthDialogListenerImpl(Activity activity)
	{
		this.activity = activity;

	}

	@Override
	public void onComplete(Bundle values)
	{

		if (activity instanceof WeiboMain)
		{
			WeiboMain weiboMain = (WeiboMain) activity;
			int type = Tools.getGlobalObject(activity).getCurrentFace();
			WeiboListAdapter weiboListAdapter = null;
			long uid = Long.parseLong(values.getString("uid"));

			User user = WeiboManager.getUser(weiboMain, uid);
			if (user != null)
			{

				weiboMain.username.setText(user.name);
				StorageManager.setValue(activity, "uid", uid);

			}
			switch (type)
			{
				case FACE_HOME:
					weiboListAdapter = WeiboData.loadWeiboListData(activity,
							type, weiboMain.homeList);
					weiboMain.data.homeTimelineAdapter = weiboListAdapter;

					break;
				case FACE_MESSAGE_AT:
					weiboListAdapter = WeiboData.loadWeiboListData(activity,
							type, weiboMain.messageAtListView);
					weiboMain.data.mentionAdapter = (WeiboRefreshListAdapter) weiboListAdapter;
				default:
					break;
			}
			weiboMain.data.imageWorkQueueMonitor = Tools.getGlobalObject(
					weiboMain).getImageWorkQueueMonitor(weiboMain);
			weiboMain.data.taskWorkQueueMonitor = Tools.getGlobalObject(
					weiboMain).getTaskWorkQueueMonitor(activity);
			weiboMain.data.imageWorkQueueMonitor.addDoneAndProcess(type,
					weiboListAdapter);

		}
	}

	@Override
	public void onError(DialogError e)
	{
		activity.finish();
	}

	@Override
	public void onCancel()
	{
		activity.finish();
	}

	@Override
	public void onWeiboException(WeiboException e)
	{
		activity.finish();
	}

}
