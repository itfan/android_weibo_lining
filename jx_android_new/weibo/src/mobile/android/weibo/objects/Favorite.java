package mobile.android.weibo.objects;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.Html;
import android.widget.SimpleAdapter;

import mobile.android.weibo.interfaces.WeiboObject;
import mobile.android.weibo.library.Tools;

public class Favorite implements WeiboObject, Serializable
{
	/** 收藏时间 */
	public String favorited_time;
	/** 收藏的微博信息字段 */
	public Status status;

	/** 获取Date形式的创建时间 */
	public Date getCreatedAt()
	{
		return Tools.strToDate(status.created_at);
	}

	public String getFormatCreatedAt()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(getCreatedAt());
	}

	/** 文本形式的source， */
	private String text_source;

	/** 获取文本形式的source */
	public String getTextSource()
	{
		if (text_source == null)
		{
			text_source = Html.fromHtml(status.source).toString();

		}

		return text_source;

	}
}
