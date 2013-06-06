package mobile.android.weibo.library;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import mobile.android.weibo.interfaces.WeiboObject;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class JSONAndObject
{

	// 使用该方法时需要先创建一个Object，传入第1个参数
	public static Object convertSingleObject(Object obj, String json)
	{

		if (obj == null || json == null)
			return obj;
		try
		{
			// 只使用public类型字段
			Field[] fields = obj.getClass().getFields();
			if (fields != null)
			{
				JSONObject jsonObject = new JSONObject(json);

				for (Field field : fields)
				{

					try
					{
						// 如果json中没有相应的key，会抛出异常，继续扫描下一个key
						Object objValue = jsonObject.get(field.getName());
						// 字符串类型
						if (field.getType() == String.class)
						{
							field.set(obj, String.valueOf(objValue));
						}
						// long类型
						else if (field.getType() == long.class)
						{
							field.set(obj,
									Long.valueOf(String.valueOf(objValue)));
						}
						// int类型
						else if (field.getType() == int.class)
						{
							field.set(obj,
									Integer.valueOf(String.valueOf(objValue)));
						}
						// boolean类型
						else if (field.getType() == boolean.class)
						{
							field.set(obj, Boolean.getBoolean(String
									.valueOf(objValue)));
						}
						// Object类型(WeiboObject类型）
						else
						{
							Object fieldObject = field.getType().newInstance();
							if (fieldObject instanceof WeiboObject)
							{
								convertSingleObject(fieldObject,
										String.valueOf(objValue));
								field.set(obj, fieldObject);
							}
						}

					}
					catch (Exception e)
					{
						// TODO: handle exception
					}

				}

			}

		}
		catch (Exception e)
		{

		}
		return obj;
	}

	// 将json字符串转换为List
	public static List convert(Class c, String json, String propertyName)
	{
		List objs = null;

		if (c == null || json == null)
			return objs;
		try
		{

			// 只使用public类型字段
			Field[] fields = c.getFields();
			if (fields != null)
			{

				String jsonStr = json;
				if (propertyName != null)
				{
					JSONObject jsonObject = new JSONObject(json);
					jsonStr = jsonObject.get(propertyName).toString();
				}

				JSONArray jsonArray = new JSONArray(jsonStr);
				objs = new ArrayList();

				for (int i = 0; i < jsonArray.length(); i++)
				{
					Object obj = c.newInstance();
					objs.add(obj);
					convertSingleObject(obj, jsonArray.getString(i));
				}

			}
		}
		catch (Exception e)
		{
			Log.d("convert", e.getMessage());
		}
		return objs;

	}

	public static String convertSingleObjectToJson(Object obj)
	{
		String json = null;
		if (obj == null)
		{
			return json;
		}
		Field[] fields = obj.getClass().getFields();
		json = "{";
		for (int i = 0; i < fields.length; i++)
		{
			try
			{
				Field field = fields[i];
				if (field.getType() == String.class)
				{
					String temp = ((field.get(obj) == null) ? "" : String
							.valueOf(field.get(obj)));

					temp = temp.replaceAll("\"", "\\\\\"");
					json += "\"" + field.getName() + "\":\"" + temp + "\"";
				}
				// long类型
				else if (field.getType() == long.class)
				{
					json += "\"" + field.getName() + "\":" + field.getLong(obj);
				}
				// int类型
				else if (field.getType() == int.class)
				{
					json += "\"" + field.getName() + "\":" + field.getInt(obj);
				}
				// boolean类型
				else if (field.getType() == boolean.class)
				{
					json += "\"" + field.getName() + "\":"
							+ field.getBoolean(obj);
				}
				// Object类型(WeiboObject类型）
				else
				{
					Object fieldObject = field.get(obj);
					if (fieldObject instanceof WeiboObject)
					{
						json += "\"" + field.getName() + "\":"
								+ convertSingleObjectToJson(fieldObject);

					}
					else
					{
						continue;
					}
				}
				if (i < fields.length - 1)
				{
					json += ",";
				}
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

		}
		json += "}";
		return json;
	}

	public static String convertObjectToJson(Object obj, String propertyName)
	{
		String json = null;
		if (obj == null)
			return json;
		if (obj instanceof List)
		{
			List list = (List) obj;
			if (propertyName != null)
			{
				// 包含一个属性的对象，这个属性是对象数组
				json = "{\"" + propertyName + "\":[";
			}
			else
			{
				// 对象数组
				json = "[";
			}
			for (int i = 0; i < list.size(); i++)
			{
				Object item = list.get(i);
				json += convertSingleObjectToJson(item);
				if (i < list.size() - 1)
					json += ",";

			}
			if (propertyName != null)
			{
				json += "]}";
			}
			else
			{
				json = "]";
			}
		}
		return json;
	}
}
