package mobile.android.jx.call.aidl;

import java.lang.reflect.Method;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class InCallReceiver extends BroadcastReceiver
{
 
	@Override
	public void onReceive(Context context, Intent intent)
	{

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Service.TELEPHONY_SERVICE);
		switch (tm.getCallState())
		{
			case TelephonyManager.CALL_STATE_RINGING: // 响铃
				// 获得来电号码
				String incomingNumber = intent
						.getStringExtra("incoming_number");
				if ("12345678".equals(incomingNumber))
				{
					try
					{
						
						
						TelephonyManager telephonyManager = (TelephonyManager) context
								.getSystemService(Service.TELEPHONY_SERVICE);
						Class<TelephonyManager> telephonyManagerClass = TelephonyManager.class;

						Method telephonyMethod = telephonyManagerClass
								.getDeclaredMethod("getITelephony",
										(Class[]) null);
						telephonyMethod.setAccessible(true);

						Object obj =  telephonyMethod
							.invoke(telephonyManager, (Object[]) null);
						Method endCallMethod =  obj.getClass().getMethod("endCall", null);
						endCallMethod.setAccessible(true);
						endCallMethod.invoke(obj, null);
						

					}
					catch (Exception e)
					{
						Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
				break;

		}

	}

}
