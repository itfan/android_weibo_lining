package mobile.android.weibo.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import mobile.android.weibo.R.string;
import mobile.android.weibo.WeiboMain;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.objects.Comment;
import mobile.android.weibo.objects.Status;

public class StorageManager implements Const
{
	public static void saveList(List list, int type)

	{
		try
		{
			saveList(list, PATH_STORAGE, type);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	public static void saveList(List list, String path, int type)
			throws Exception
	{
		if (list == null || list.size() == 0)
			return;
		String json = "";
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;

		switch (type)
		{
			case FACE_HOME:
				json = JSONAndObject.convertObjectToJson(list, "statuses");
				fos = new FileOutputStream(path + "/home_timeline");
				osw = new OutputStreamWriter(fos, "utf-8");
				osw.write(json);
				osw.close();
				break;
			case FACE_MESSAGE_AT:
				Log.d(" FACE_MESSAGE_AT", "ddddd");
				json = JSONAndObject.convertObjectToJson(list, "statuses");
				fos = new FileOutputStream(path + "/mention_timeline");
				osw = new OutputStreamWriter(fos, "utf-8");
				osw.write(json);
				osw.close();
				break;
			case FACE_MESSAGE_COMMENT:
				json = JSONAndObject.convertObjectToJson(list, "comments");
				fos = new FileOutputStream(path + "/comment_timeline");
				osw = new OutputStreamWriter(fos, "utf-8");
				osw.write(json);
				osw.close();
				break;
			case FACE_MESSAGE_FAVORITE:
				json = JSONAndObject.convertObjectToJson(list, "statuses");
				fos = new FileOutputStream(path + "/favorite");
				osw = new OutputStreamWriter(fos, "utf-8");
				osw.write(json);
				osw.close();
				break;
			default:
				break;
		}

	}

	public static List loadList(Activity activity, String path, int type)
			throws Exception
	{
		List list = null;
		String json = "";
		File file = new File(path);
		if (!file.exists())
			return list;

		switch (type)
		{
			case FACE_HOME:
				json = readFile(path + "/home_timeline");

				if (json == null)
				{
					list = WeiboManager.getHomeTimeline(activity);
				}
				else
				{
					list = JSONAndObject
							.convert(Status.class, json, "statuses");
				}
				break;
			case FACE_MESSAGE_AT:
				json = readFile(path + "/mention_timeline");

				if (json == null)
				{
					list = WeiboManager.getHomeTimeline(activity);
				}
				else
				{
					list = JSONAndObject
							.convert(Status.class, json, "statuses");
				}
				break;
			case FACE_MESSAGE_COMMENT:
				json = readFile(path + "/comment_timeline");

				if (json == null)
				{
					list = WeiboManager.getCommentTimeline(activity);
				}
				else
				{
					list = JSONAndObject.convert(Comment.class, json,
							"comments");
				}
				break;
			case FACE_MESSAGE_FAVORITE:
				json = readFile(path + "/favorite");

				if (json == null)
				{
					list = WeiboManager.getFavorites(activity);
				}
				else
				{
					list = JSONAndObject
							.convert(Status.class, json, "statuses");
					
				}
				break;
			default:
				break;
		}
		return list;

	}

	public static List loadList(Activity activity, int type)
	{
		try
		{

			return loadList(activity, PATH_STORAGE, type);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static String readFile(String filename)
	{
		StringBuilder result = new StringBuilder();
		if (!new File(filename).exists())
			return null;
		try
		{
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String temp = "";
			while ((temp = br.readLine()) != null)
			{
				result.append(temp);
			}
			isr.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return result.toString();
	}

	public static String saveBitmap(Bitmap bitmap, String filename)
	{
		String fn = filename;
		if (fn == null)
		{
			File file = new File(PATH_IMAGE);
			if (!file.exists())
				file.mkdirs();
			fn = PATH_IMAGE + "/" + String.valueOf(new Random().nextLong())
					+ ".jpg";
			while (new File(fn).exists())
			{
				fn = PATH_IMAGE + "/" + String.valueOf(new Random().nextLong())
						+ ".jpg";
			}
		}
		try
		{
			FileOutputStream fos = new FileOutputStream(fn);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

		return fn;
	}
	public static void setValue(Context context, String key, String value)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putString(key, value).commit();
		
	}
	public static void setValue(Context context, String key, long value)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		sharedPreferences.edit().putLong(key, value).commit();
		
	}
	public static String getValue(Context context, String key, String defaultValue)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, defaultValue);
	}
	public static long getValue(Context context, String key, long defaultValue)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(key, defaultValue);
	}
	public static String saveBitmap(Bitmap bitmap)
	{
		return saveBitmap(bitmap, null);
	}
}
