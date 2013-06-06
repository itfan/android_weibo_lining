package mobile.android.photo.share;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;

public class Consumer
{
	public static final int CONSUMER_REQUEST_CODE = 10000; 
	public static String redirectUrl = "";
	public static String consumerKey = "";
	public static String consumerSecret = "";
 
	public static boolean verify(Activity activity)
	{
		boolean result = true;
		if ("".equals(consumerKey) || "".equals(consumerSecret) || "".equals(redirectUrl) )
		{
			loadConsumerFromFile();  
		}
		if ("".equals(consumerKey) || "".equals(consumerSecret) || "".equals(redirectUrl) )
		{
			Intent intent = new Intent(activity, ConsumerActivity.class);
			activity.startActivityForResult(intent,CONSUMER_REQUEST_CODE);
			result = false;
		
		}
		return result;

	}

	private static void loadConsumerFromFile()
	{
		try
		{
			FileInputStream fis = new FileInputStream("/sdcard/consumer.ini");
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			String value = br.readLine();
			if(value != null)
			{
				consumerKey = value;
			}
			value = br.readLine();
			if(value != null)
			{
				consumerSecret = value;
			}
			value = br.readLine();
			if(value != null)
			{
				redirectUrl = value;
			}
			fis.close();
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}

	}
}
