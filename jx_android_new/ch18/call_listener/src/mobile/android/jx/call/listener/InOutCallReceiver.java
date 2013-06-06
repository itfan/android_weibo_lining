package mobile.android.jx.call.listener;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class InOutCallReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(final Context context, final Intent intent)
	{
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			String outcommingNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Toast.makeText(context, outcommingNumber, Toast.LENGTH_LONG).show();
		}
		else
		{
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			
			String incomingNumber = intent.getStringExtra("incoming_number");
			switch (tm.getCallState())
			{
				case TelephonyManager.CALL_STATE_RINGING: // 来电响铃
					Toast.makeText(context,
							"CALL_STATE_RINGING：" + incomingNumber,
							Toast.LENGTH_SHORT).show();
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机
					Toast.makeText(context,
							"CALL_STATE_OFFHOOK：" + incomingNumber,
							Toast.LENGTH_SHORT).show();
					break;
				case TelephonyManager.CALL_STATE_IDLE: // 挂机
					Toast.makeText(context,
							"CALL_STATE_IDLE：" + incomingNumber,
							Toast.LENGTH_SHORT).show();

					break;
			}
		}
	}
}
