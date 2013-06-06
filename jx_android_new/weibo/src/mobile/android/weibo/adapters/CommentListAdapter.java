package mobile.android.weibo.adapters;

import java.util.List;

import mobile.android.weibo.R;
import mobile.android.weibo.interfaces.Const;
import mobile.android.weibo.library.StorageManager;
import mobile.android.weibo.library.Tools;
import mobile.android.weibo.objects.Comment;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentListAdapter extends BaseAdapter implements Const
{
	private Activity activity;
	private LayoutInflater layoutInflater;
	private List<Comment> comments;

	public CommentListAdapter(Activity activity, List<Comment> comments)
	{
		this.activity = activity;
		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.comments = comments;
		StorageManager.saveList(comments, FACE_MESSAGE_COMMENT);

	}

	@Override
	public int getCount()
	{
		return comments.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = layoutInflater.inflate(R.layout.comment_list_viewer_item,
					null);
		}

		TextView name = (TextView) convertView.findViewById(R.id.textview_name);
		TextView createdAt = (TextView) convertView
				.findViewById(R.id.textview_created_at);
		TextView commentText = (TextView) convertView
				.findViewById(R.id.textview_comment_text);
		TextView source = (TextView) convertView
				.findViewById(R.id.textview_source);
		Comment comment = comments.get(position);
		name.setText(comment.user.name);
		createdAt.setText(comment.getFormatCreatedAt());
		commentText.setText(Tools.changeTextToFace(activity,
				Html.fromHtml(Tools.atBlue(comment.text))));
		source.setText("来自  " + comment.getTextSource());

		return convertView;
	}

}
