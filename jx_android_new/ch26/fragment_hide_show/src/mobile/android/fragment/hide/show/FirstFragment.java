package mobile.android.fragment.hide.show;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment
{
	TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.labeled_text_edit, container, false);
		View tv = v.findViewById(R.id.msg);
		((TextView) tv).setText("нд╠╬©Р1");

		
		return v;
	}

	
}