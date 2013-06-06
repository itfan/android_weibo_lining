package mobile.android.fragment.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyFragment1 extends Fragment
{

	// 输出当前调用writeLog方法的方法名和类名
	private void writeLog()
	{
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		String className = stack[1].getClassName();
		String methodName = stack[1].getMethodName();
		Log.d(methodName, className);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		writeLog();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		writeLog();
		View view = inflater.inflate(R.layout.fragment1, container, false);
		return view;
	}

	@Override
	public void onAttach(Activity activity)
	{
		writeLog();
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		writeLog();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy()
	{
		writeLog();
		super.onDestroy();
	}

	@Override
	public void onDestroyView()
	{
		writeLog();
		super.onDestroyView();
	}

	@Override
	public void onDetach()
	{
		writeLog();
		super.onDetach();
	}

	@Override
	public void onPause()
	{
		writeLog();
		super.onPause();
	}

	@Override
	public void onResume()
	{
		writeLog();
		super.onResume();
	}

	@Override
	public void onStart()
	{
		writeLog();
		super.onStart();
	}

	@Override
	public void onStop()
	{
		writeLog();
		super.onStop();
	}

}
