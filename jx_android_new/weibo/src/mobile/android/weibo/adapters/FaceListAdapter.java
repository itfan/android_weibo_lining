package mobile.android.weibo.adapters;

import mobile.android.weibo.R;
import mobile.android.weibo.library.FaceMan;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceListAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;

	public FaceListAdapter(Context context)
	{
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{

		return FaceMan.getCount();
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
			convertView = layoutInflater.inflate(R.layout.face, null);
		}
		ImageView imageView = (ImageView) convertView;
		
		imageView.setImageResource(FaceMan.getFaceResourceId(position));
		return convertView;
	}

}
