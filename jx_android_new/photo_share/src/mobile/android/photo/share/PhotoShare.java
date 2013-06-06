package mobile.android.photo.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

public class PhotoShare extends Activity
{
	public static Weibo weibo;
	private static Intent intent;
	private static String state = "Start";

	class AuthDialogListener implements WeiboDialogListener
	{

		@Override
		public void onComplete(Bundle values)
		{

			monitorOnOff();

		}

		@Override
		public void onError(DialogError e)
		{
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel()
		{
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e)
		{
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	private void monitorOnOff()
	{

		Button btnStartMonitor = (Button) findViewById(R.id.btnStartMonitor);
		if ("Start".equals(btnStartMonitor.getText()))
		{
			state = "Stop";
			
			startService(intent);
			setTitle("服务已开启");
		}
		else
		{
			state = "Start";
			stopService(intent);
			setTitle("服务已停止");
		}
		btnStartMonitor.setText(state);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if(intent == null)
			intent = new Intent(PhotoShare.this, PhotoShareService.class);
		Button btnStartMonitor = (Button) findViewById(R.id.btnStartMonitor);
		btnStartMonitor.setText(state);
		Consumer.verify(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == Consumer.CONSUMER_REQUEST_CODE)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClick_Monitor(View view) throws Exception
	{
		if (weibo == null || !weibo.isSessionValid())
		{

			weibo = Weibo.getInstance();
			weibo.setupConsumerConfig(Consumer.consumerKey, Consumer.consumerSecret);

			weibo.setRedirectUrl(Consumer.redirectUrl);
			weibo.authorize(this, new AuthDialogListener());

		}  
		else  
		{
			monitorOnOff();
		}

	}
}