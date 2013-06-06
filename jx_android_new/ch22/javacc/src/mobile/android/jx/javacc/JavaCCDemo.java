package mobile.android.jx.javacc;

import java.io.StringReader;

import mobile.android.jx.javacc.parser.MyParser;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class JavaCCDemo extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void onClick_Parser(View view)
	{

		MyParser myParser = new MyParser(new StringReader("(((((())))))"));
		try
		{
			Toast.makeText(this, "嵌套层:" + myParser.Input(), Toast.LENGTH_LONG)
					.show();
		}
		catch (Exception e)
		{
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

		}

	}
}