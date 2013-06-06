package mobile.android.jx.client.socket;

import java.io.InputStream;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClientSocket extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void onClick_VisitWebServer(View view)
    {
    	TextView textView = (TextView)findViewById(R.id.textview);
    	try
    	{
        	Socket socket = new Socket("192.168.17.81", 4321);
        	InputStream is = socket.getInputStream();
        	byte[] buffer =  new byte[1024];
        	int count = is.read(buffer);
        	String result = new String(buffer, 0, count, "utf-8");
        	result = result.replaceAll("\\r", "");
        	is.close();
        	socket.close();    		
        	textView.setText(result);
    	}
    	catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
}
