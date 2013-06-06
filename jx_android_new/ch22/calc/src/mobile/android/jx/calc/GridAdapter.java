package mobile.android.jx.calc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class GridAdapter extends BaseAdapter
{
	private LayoutInflater layoutInflater;
	private Context context;
	private final String[] buttonTexts = new String[]
	{ "(", ")", "退格", "清除", "7", "8", "9", "+", "4", "5", "6", "-", "1", "2",
			"3", "*", "0", ".", "PI", "/", "sin", "cos", "%", "=" };

	public GridAdapter(Context context)
	{
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return buttonTexts.length;
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
		Button button = (Button) layoutInflater.inflate(R.layout.button, null);

		button.setText(buttonTexts[position]);

		button.setOnClickListener((OnClickListener) context);

		return button;
	}

}
