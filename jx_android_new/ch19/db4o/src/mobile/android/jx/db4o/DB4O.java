package mobile.android.jx.db4o;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

public class DB4O extends Activity
{
	private ObjectContainer db;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),
				"/sdcard/db4o.data");

	}

	public void onClick_WriteData(View view)
	{
		Student student = new Student(1, "John", 89);
		db.store(student);
		student = new Student(2, "Mary", 98);
		db.store(student);
		student = new Student(3, "王军", 67);
		db.store(student);
		db.commit();
		Toast.makeText(this, "成功存储了数据.", Toast.LENGTH_SHORT).show();
	}

	public void onClick_QueryAllData(View view)
	{
		ObjectSet<Student> result = db.queryByExample(new Student());
		String s = "";
		while (result.hasNext())
		{
			Student student = result.next();
			s += student.getName() + ":" + student.getGrade() + "\n";
		}
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	public void onClick_QueryData(View view)
	{
		ObjectSet<Student> result = db.queryByExample(new Student(3, null, 0));
		String s = "";
		while (result.hasNext())
		{
			Student student = result.next();
			s += student.getName() + ":" + student.getGrade() + "\n";
		}
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
		result = db.queryByExample(new Student(0, "Mary", 0));
		s = "";
		while (result.hasNext())
		{
			Student student = result.next();
			s += student.getName() + ":" + student.getGrade() + "\n";
		}
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	public void onClick_UpdateData(View view) 
	{
		ObjectSet<Student> result = db.queryByExample(new Student(3, null, 0));
		if (result.hasNext())
		{
			Student student = result.next();
			student.setName("小强");
			db.store(student);
			db.commit();
			Toast.makeText(this, "更新成功.", Toast.LENGTH_SHORT).show();
		}
	}
	public void onClick_DeleteData(View view) 
	{
		ObjectSet<Student> result = db.queryByExample(new Student(3, null, 0));
		if (result.hasNext())
		{
			Student student = result.next();
			
			db.delete(student);
			db.commit();
			Toast.makeText(this, "删除成功.", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onDestroy()
	{
		db.close();
		super.onDestroy();
	}

}