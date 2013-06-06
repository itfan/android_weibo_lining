package mobile.android.jx.send.mms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class SendMMS extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick_SendMMS(View view)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("address", "12345");
		intent.putExtra("compose_mode", false);
		intent.putExtra("exit_on_sent", true);
		intent.putExtra("subject", "≤ –≈≤‚ ‘");
		intent.putExtra("sms_body", "≤ –≈ƒ⁄»› ");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/a.jpg"));
		intent.setClassName("com.android.mms",
				"com.android.mms.ui.ComposeMessageActivity");
		intent.setType("image/jpeg");

		startActivity(Intent.createChooser(intent, "Send MMS To"));
	}
}