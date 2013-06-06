package mobile.android.alert.dialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_dialog);

		View tv = findViewById(R.id.text);
		((TextView) tv).setText("DialogFragment显示对话框演示");

		Button button = (Button) findViewById(R.id.show);
		button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				showDialog();
			}
		});

		
	}

	void showDialog()
	{
		DialogFragment newFragment = MyAlertDialogFragment
				.newInstance(R.string.app_name);
		newFragment.show(getFragmentManager(), "dialog");
	}



}