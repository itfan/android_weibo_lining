package mobile.android.alert.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class MyAlertDialogFragment extends DialogFragment
{
	public static MyAlertDialogFragment newInstance(int title)
	{
		MyAlertDialogFragment frag = new MyAlertDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args); 
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		int title = getArguments().getInt("title");

		return new AlertDialog.Builder(getActivity())
				.setIcon(R.drawable.ic_launcher)
				.setTitle(title)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								
							}
						}).create();
	}
}
