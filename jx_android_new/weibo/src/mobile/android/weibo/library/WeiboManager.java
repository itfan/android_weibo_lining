package mobile.android.weibo.library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mobile.android.weibo.Consumer;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.objects.Comment;
import mobile.android.weibo.objects.Favorite;
import mobile.android.weibo.objects.Status;
import mobile.android.weibo.objects.User;
import mobile.android.weibo.workqueue.DoneAndProcess;
import mobile.android.weibo.workqueue.WorkQueueStorage;
import mobile.android.weibo.workqueue.task.PullFileTask;
import android.app.Activity;

import com.weibo.net.AsyncWeiboRunner;
import com.weibo.net.AsyncWeiboRunner.RequestListener;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class WeiboManager implements Const
{

	public static List<Status> getHomeTimeline(Activity activity, long sinceId,
			long maxId, int count)
	{
		return getHomeTimeline(activity, sinceId, maxId, count, false, null);

	}

	public static void getHomeTimelineAsync(Activity activity,
			RequestListener listener)
	{
		getHomeTimeline(activity, 0, 0, 0, true, listener);
	}

	public static List<Status> getHomeTimeline(Activity activity, long sinceId,
			long maxId, int count, boolean async, RequestListener listener)
	{
		String url = Weibo.SERVER + "statuses/home_timeline.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);

		if (sinceId != 0)
			bundle.add("since_id", String.valueOf(sinceId));
		if (maxId != 0)
			bundle.add("max_id", String.valueOf(maxId));
		if (count != 0)
			bundle.add("count", String.valueOf(count));
		List<Status> statuses = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				statuses = JSONAndObject
						.convert(Status.class, json, "statuses");
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return statuses;
	}

	public static List<Status> getHomeTimeline(Activity activity)
	{
		return getHomeTimeline(activity, 0, 0, DEFAULT_STATUS_COUNT);
	}

	public static List<Comment> getComments(Activity activity, long status_id,
			long sinceId, long maxId, int count, boolean async,
			RequestListener listener)
	{
		String url = Weibo.SERVER + "comments/show.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		bundle.add("id", String.valueOf(status_id));
		if (sinceId != 0)
			bundle.add("since_id", String.valueOf(sinceId));
		if (maxId != 0)
			bundle.add("max_id", String.valueOf(maxId));
		if (count != 0)
			bundle.add("count", String.valueOf(count));
		List objs = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				objs = JSONAndObject.convert(Comment.class, json, "comments");
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return objs;
	}

	public static void getCommentsAsync(Activity activity, long id,
			RequestListener listener)
	{
		getComments(activity, id, 0, 0, 0, true, listener);
	}

	public static List<Status> getMentions(Activity activity, long sinceId,
			long maxId, int count, boolean async, RequestListener listener)
	{

		String url = Weibo.SERVER + "statuses/mentions.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		if (sinceId != 0)
			bundle.add("since_id", String.valueOf(sinceId));
		if (maxId != 0)
			bundle.add("max_id", String.valueOf(maxId));
		if (count != 0)
			bundle.add("count", String.valueOf(count));
		List objs = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				objs = JSONAndObject.convert(Status.class, json, "statuses");
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return objs;
	}

	public static List<Status> getMentions(Activity activity)
	{
		return getMentions(activity, 0, 0, 0, false, null);
	}

	public static void getMentionsAsync(Activity activity,
			RequestListener listener)
	{
		getMentions(activity, 0, 0, 50, true, listener);

	}

	public static void getCommentTimelineAsync(Activity activity,
			RequestListener listener)
	{
		getCommentTimeline(activity, 0, 0, 0, 0, true, listener);
	}

	public static List<Comment> getCommentTimeline(Activity activity)
	{
		return getCommentTimeline(activity, 0, 0, 0, 0, false, null);
	}

	public static List<Comment> getCommentTimeline(Activity activity,
			long status_id, long sinceId, long maxId, int count, boolean async,
			RequestListener listener)
	{
		String url = Weibo.SERVER + "comments/timeline.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		if (sinceId != 0)
			bundle.add("since_id", String.valueOf(sinceId));
		if (maxId != 0)
			bundle.add("max_id", String.valueOf(maxId));
		if (count != 0)
			bundle.add("count", String.valueOf(count));
		List objs = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				objs = JSONAndObject.convert(Comment.class, json, "comments");
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return objs;
	}

	public static void getFavoritesAsync(Activity activity,
			RequestListener listener)
	{
		getFavorites(activity, 50, true, listener);
	}

	public static List<Status> getFavorites(Activity activity)
	{
		return getFavorites(activity, 0, false, null);
	}

	public static List<Status> FavoriteToStatus(List<Favorite> favorites)
	{
		List<Status> statuses = new ArrayList<Status>();
		for (Favorite favorite : favorites)
		{
			favorite.status.favorited = true;
			statuses.add(favorite.status);
		}
		return statuses;

	}

	public static List<Status> getFavorites(Activity activity, int count,
			boolean async, RequestListener listener)
	{
		String url = Weibo.SERVER + "favorites.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);

		if (count != 0)
			bundle.add("count", String.valueOf(count));
		List<Favorite> favorites = null;
		List<Status> statuses = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				favorites = JSONAndObject.convert(Favorite.class, json,
						"favorites");
				statuses = FavoriteToStatus(favorites);
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return statuses;
	}

	public static Status getStatus(Activity activity, long id)
	{
		return getStatus(activity, id, null, null);
	}

	public static void getStatusAsync(Activity activity, long id,
			RequestListener listener)
	{
		getStatus(activity, id, null, listener);
	}

	public static Status getStatus(Activity activity, long id, Status status,
			RequestListener listener)
	{

		if (id > 0)
		{
			String url = Weibo.SERVER + "statuses/show.json";
			Weibo weibo = Tools.getWeibo(activity);
			if (weibo == null || !weibo.isSessionValid())
				return null;
			WeiboParameters bundle = new WeiboParameters();
			bundle.add("source", Consumer.consumerKey);
			bundle.add("id", String.valueOf(id));
			try
			{
				if (listener == null)
				{
					String json = weibo.request(activity, url, bundle, "GET",
							weibo.getAccessToken());
					if (status == null)
						status = new Status();
					JSONAndObject.convertSingleObject(status, json);
				}
				else
				{
					AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(
							weibo);
					asyncWeiboRunner.request(activity, url, bundle, "GET",
							listener);
				}
			}
			catch (Exception e)
			{

			}
		}
		return status;
	}

	public static User getUser(Activity activity, long uid, String screen_name,
			boolean async, RequestListener listener)
	{
		String url = Weibo.SERVER + "users/show.json";
		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		User user = null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		if (uid > 0)
		{
			bundle.add("uid", String.valueOf(uid));
		}
		else if (screen_name != null)
		{
			bundle.add("screen_name", screen_name);
		}
		else
		{
			return user;
		}
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				user = new User();
				JSONAndObject.convertSingleObject((Object) user, json);
			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}

		}
		catch (Exception e)
		{

		}
		return user;
	}

	public static void getUserAsync(Activity activity, long uid,
			RequestListener listener)
	{
		getUser(activity, uid, null, true, listener);
	}

	public static User getUser(Activity activity, long uid)
	{
		return getUser(activity, uid, null, false, null);
	}

	public static User getUser(Activity activity, String screen_name)
	{
		return getUser(activity, 0, screen_name, false, null);
	}

	public static boolean hasPicture(Status status)
	{
		if (status.thumbnail_pic != null && !"".equals(status.thumbnail_pic))
			return true;
		if (status.retweeted_status != null)
		{
			if (status.retweeted_status.thumbnail_pic != null
					&& !"".equals(status.retweeted_status.thumbnail_pic))
			{
				return true;
			}
		}
		return false;
	}

	// 首先从SD卡缓存区获取图像路径，如果不存在，则将url添加到工作队列，并且返回null
	// url参数是web地址，而返回的是缓存区的文件名（用url的hashcode命名）
	public static String getImageurl(Activity activity, String url)
	{
		return getImageurl(activity, url, null);
	}

	public static String getImageurl(Activity activity, String url,
			DoneAndProcess doneAndProcess)
	{
		String result = null;
		if (url == null || "".equals(url))
			return result;
		result = PATH_FILE_CACHE + "/" + url.hashCode();
		File file = new File(PATH_FILE_CACHE + "/" + url.hashCode());
		if (file.exists())
		{
			return result;
		}
		else
		{

			WorkQueueStorage workQueueStorage = Tools.getGlobalObject(activity)
					.getWorkQueueStorage();

			if (workQueueStorage != null)
			{
				if (doneAndProcess == null)
				{
					workQueueStorage.addDoingWebFileUrl(url);
				}
				else
				{
					PullFileTask pullFileTask = new PullFileTask();
					pullFileTask.doneAndProcess = doneAndProcess;
					pullFileTask.fileUrl = url;
					workQueueStorage.addTask(pullFileTask);
				}
			}

			result = null;
		}
		return result;
	}

	public static String update(Activity activity, Weibo weibo, String text,
			String file) throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());
		bundle.add("status", text);
		String url = Weibo.SERVER + "statuses/update.json";
		if (file != null)
		{
			bundle.add("pic", file);
			url = Weibo.SERVER + "statuses/upload.json";
		}
		String rlt = "";

		rlt = weibo.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				weibo.getAccessToken());
		return rlt;
	}

	public static String update(Activity activity, Weibo weibo, String text)
			throws WeiboException
	{

		return update(activity, weibo, text, null);
	}

	// isComment 0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 。
	public static String repost(Activity activity, Weibo weibo, long id,
			String text, int isComment) throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());
		bundle.add("id", String.valueOf(id));
		bundle.add("status", text);
		bundle.add("is_comment", String.valueOf(isComment));
		String url = Weibo.SERVER + "statuses/repost.json";
		String rlt = "";

		rlt = weibo.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				weibo.getAccessToken());
		return rlt;
	}

	// commentOri 当评论转发微博时，是否评论给原微博，0：否、1：是，默认为0。
	public static String comment(Activity activity, Weibo weibo, long id,
			String comment, int commentOri) throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());
		bundle.add("id", String.valueOf(id));
		bundle.add("comment", comment);
		bundle.add("comment_ori", String.valueOf(commentOri));
		String url = Weibo.SERVER + "comments/create.json";
		String rlt = "";

		rlt = weibo.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				weibo.getAccessToken());
		return rlt;
	}

	public static String favorite(Activity activity, Weibo weibo, long id,
			boolean fav) throws WeiboException
	{
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", weibo.getAppKey());
		bundle.add("id", String.valueOf(id));

		String url = Weibo.SERVER + "favorites/create.json";
		if (!fav)
		{
			url = Weibo.SERVER + "favorites/destroy.json";
		}
		String rlt = "";

		rlt = weibo.request(activity, url, bundle, Utility.HTTPMETHOD_POST,
				weibo.getAccessToken());
		return rlt;

	}

	public static List<Comment> getHotComments(Activity activity,
			boolean async, RequestListener listener)
	{
		String url = Weibo.SERVER + "statuses/hot/comments_daily.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		bundle.add("count", "50");
		List<Comment> comments = null;

		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				comments = JSONAndObject.convert(Comment.class, json, null);

			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return comments;
	}

	public static void getHotCommentsAsync(Activity activity,
			RequestListener listener)
	{
		getHotComments(activity, true, listener);
	}

	public static List<Status> getHotStatuses(Activity activity, boolean async,
			RequestListener listener)
	{

		String url = Weibo.SERVER + "statuses/hot/repost_daily.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		bundle.add("count", "50");
		List<Status> statuses = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				statuses = JSONAndObject.convert(Status.class, json, null);

			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{

		}
		return statuses;
	}

	public static void getHotStatusesAsync(Activity activity,
			RequestListener listener)
	{
		getHotStatuses(activity, true, listener);
	}

	public static List<Status> getHotFavorites(Activity activity,
			boolean async, RequestListener listener)
	{

		String url = Weibo.SERVER + "suggestions/favorites/hot.json";

		Weibo weibo = Tools.getWeibo(activity);
		if (weibo == null || !weibo.isSessionValid())
			return null;
		WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", Consumer.consumerKey);
		bundle.add("count", "50");
		List<Status> statuses = null;
		try
		{
			if (!async)
			{
				String json = weibo.request(activity, url, bundle, "GET",
						weibo.getAccessToken());

				statuses = JSONAndObject.convert(Status.class, json, null);

			}
			else
			{
				AsyncWeiboRunner asyncWeiboRunner = new AsyncWeiboRunner(weibo);
				asyncWeiboRunner
						.request(activity, url, bundle, "GET", listener);
			}
		}
		catch (Exception e)
		{
 
		}   
		return statuses;
	}

	public static void getHotFavoritesAsync(Activity activity,
			RequestListener listener)
	{
		getHotFavorites(activity, true, listener);
	}
}
