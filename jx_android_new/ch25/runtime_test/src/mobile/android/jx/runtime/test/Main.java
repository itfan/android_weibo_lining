package mobile.android.jx.runtime.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Toast;

public class Main extends Activity
{
	private List<Integer> list1 = new ArrayList<Integer>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);


		setContentView(R.layout.main);
	}

	public void test1() 
	{
		for (int i = 0; i < 10000; i++)
		{
			list1.add(i);
		}
	}

	public void test2()
	{
		for (int i = 0; i < 10000; i++)
			list1.get(i);
	}
    public void onClick_Test(View view)
    {
		try
		{
			// Debug.startMethodTracing("activity_trace");
			long start1 = System.currentTimeMillis();
			test1();
			long end1 = System.currentTimeMillis();

			long start2 = System.currentTimeMillis();
			test2();
			long end2 = System.currentTimeMillis();
			//Debug.stopMethodTracing();
			Toast.makeText(
					this,
					"test1方法的执行时间：" + (end1 - start1) + "毫秒\ntest2方法的执行时间："
							+ (end2 - start2) + "毫秒", Toast.LENGTH_LONG).show();

		}
		catch (Exception e)
		{

		}
    }
}
