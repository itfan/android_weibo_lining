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

	
		// ������SMS������������
		Bundle bundle = intent.getExtras();
		
		// �ж��Ƿ�������
		if (bundle != null)
		{


			// ͨ��pdus���Ի�ý��յ������ж���Ϣ
			Object[] objArray = (Object[]) bundle.get("pdus");
			// �������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С

			SmsMessage[] messages = new SmsMessage[objArray.length]; 
			
			String body = "";
			for (int i = 0; i < objArray.length; i++)
			{

				messages[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
				
				body += messages[i].getDisplayMessageBody();

			}
			String phoneNumber = messages[0].getDisplayOriginatingAddress();
			Toast.makeText(context, "�绰�ţ�" + phoneNumber + "\n" + body,
					Toast.LENGTH_LONG).show();

		}

	}
}
