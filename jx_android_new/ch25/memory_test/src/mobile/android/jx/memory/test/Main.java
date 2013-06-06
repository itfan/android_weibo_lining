package mobile.android.jx.memory.test;

import java.util.ArrayList;
import java.util.List;

import com.yarin.android.Examples_15_02.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

			long start1 = Memory.used();
			test1();
			long end1 = Memory.used();
			long start2 = Memory.used();
			test2();
			long end2 = Memory.used();
			
			Toast.makeText(
					this,
					"test1方法占用的内存：：" + (end1 - start1) + "字节\ntest2方法的占用的内存："
							+ (end2 - start2) + "字节", Toast.LENGTH_LONG).show();

		}
		catch (Exception e)
		{

		}
	}
}
