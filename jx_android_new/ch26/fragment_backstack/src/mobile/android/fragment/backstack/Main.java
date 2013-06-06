package mobile.android.fragment.backstack;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity
{

	int mStackLevel = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_stack);


		Button button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				addFragmentToStack();
			}
		});
		button = (Button) findViewById(R.id.popup_fragment);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				getFragmentManager().popBackStack();
				mStackLevel--;
			}
		});
		if (savedInstanceState == null)
		{

			Fragment newFragment = CountingFragment.newInstance(mStackLevel);
			FragmentTransaction ft = getFragmentManager().beginTransaction();

			ft.add(R.id.simple_fragment, newFragment).commit();
		}
		else
		{
			mStackLevel = savedInstanceState.getInt("level");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);
	}

	void addFragmentToStack()
	{
		mStackLevel++;


		Fragment newFragment = CountingFragment.newInstance(mStackLevel);

		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.add(R.id.simple_fragment, newFragment);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(null);

		ft.commit();
	}

	public static class CountingFragment extends Fragment
	{
		int mNum;


		static CountingFragment newInstance(int num)
		{
			CountingFragment f = new CountingFragment();

	
			Bundle args = new Bundle();
			args.putInt("num", num);
			f.setArguments(args);

			return f;
		}

		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 1;

		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View v = inflater.inflate(R.layout.fragment_view, container, false);
			View tv = v.findViewById(R.id.text);
			((TextView) tv).setText("Fragment #" + mNum);
			
			return v;
		}
	}
}