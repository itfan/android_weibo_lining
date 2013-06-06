package mobile.android.jx.call.list;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.SimpleCursorAdapter;

public class CallList extends ListActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{  
		super.onCreate(savedInstanceState);

		Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
				null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
	
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, new String[]
				{ "number" }, new int[]
				{ android.R.id.text1 });
		setListAdapter(simpleCursorAdapter);
	}
}