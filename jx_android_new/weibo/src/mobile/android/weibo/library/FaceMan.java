package mobile.android.weibo.library;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import mobile.android.weibo.R;

public class FaceMan 
{
	private static String[] faces =
	{ "[织]:217", "[浮云]:229", "[围观]:218", "[威武]:219", "[囧]:121", "[呵呵]:25",
			"[嘻嘻]:233", "[哈哈]:234", "[可爱]:259", "[可怜]:239", "[挖鼻屎]:253",
			"[吃惊]:182", "[害羞]:201", "[闭嘴]:205", "[偷笑]:247", "[爱你]:254",
			"[怒骂]:251", "[心]:279", "[ok]:102", "[耶]:271", "[good]:100",
			"[赞]:106", "[来]:277", "[弱]:273" };
    private static Map<String, Integer> faceMap = new HashMap<String, Integer>();
	static
	{
		for(int i = 0; i < getCount(); i++)
		{
			faceMap.put(getFaceText(i), getFaceResourceId(i));
		}
	}
	public static int getCount()
	{
		return faces.length;
	}

	public static String getFaceText(int index)
	{
		if (index >= getCount())
			return "";
		String text = faces[index];
		text = text.substring(0, text.indexOf(":"));
		return text;

	}

	public static int getFaceResourceId(int index)
	{
		if (index >= getCount())
			return 0;
		int resourceId = 0;
		String faceName = "face"
				+ faces[index].substring(faces[index].indexOf(":") + 1);
		try
		{
			Field field = R.drawable.class.getField(faceName);
			resourceId = Integer.parseInt(String.valueOf(field.get(null)));
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return resourceId;

	}
	public static int getResourceId(String faceText)
	{
		Object obj = faceMap.get(faceText);
		if(obj == null)
			return 0;
		return Integer.parseInt(String.valueOf(obj));
		
	}
}
