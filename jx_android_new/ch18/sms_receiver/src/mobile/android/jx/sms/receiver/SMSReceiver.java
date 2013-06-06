package mobile.android.jx.sms.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{

	
		// 接收由SMS传过来的数据
		Bundle bundle = intent.getExtras();
		
		// 判断是否有数据
		if (bundle != null)
		{


			// 通过pdus可以获得接收到的所有短信息
			Object[] objArray = (Object[]) bundle.get("pdus");
			// 构建短信对象array,并依据收到的对象长度来创建array的大小

			SmsMessage[] messages = new SmsMessage[objArray.length]; 
			
			String body = "";
			for (int i = 0; i < objArray.length; i++)
			{

				messages[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
				
				body += messages[i].getDisplayMessageBody();

			}
			String phoneNumber = messages[0].getDisplayOriginatingAddress();
			Toast.makeText(context, "电话号：" + phoneNumber + "\n" + body,
					Toast.LENGTH_LONG).show();

		}

	}
}
