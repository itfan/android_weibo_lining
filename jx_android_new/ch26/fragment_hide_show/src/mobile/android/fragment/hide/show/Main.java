package mobile.android.fragment.hide.show;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_hide_show);

		FragmentManager fm = getFragmentManager();
		addShowHideListener(R.id.frag1hide, fm.findFragmentById(R.id.fragment1));
		addShowHideListener(R.id.frag2hide, fm.findFragmentById(R.id.fragment2));
	}

	void addShowHideListener(int buttonId, final Fragment fragment)
	{
		final Button button = (Button) findViewById(buttonId);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				FragmentTransaction ft = getFragmentManager()
						.beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in,
						android.R.animator.fade_out);
				if (fragment.isHidden())
				{
					ft.show(fragment);
					button.setText("Òþ²Ø");
				}
				else
				{
					ft.hide(fragment);
					button.setText("ÏÔÊ¾");
				}
				ft.commit();
			}
		});
	}

}