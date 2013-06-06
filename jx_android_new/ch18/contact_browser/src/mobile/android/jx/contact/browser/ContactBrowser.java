package mobile.android.jx.contact.browser;

import android.app.ListActivity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ContactBrowser extends ListActivity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Cursor cursor = getContentResolver().query(
				Uri.withAppendedPath(ContactsContract.AUTHORITY_URI,
						"data/phones"), null, null, null, null);
		SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, cursor, new String[]
				{ "display_name", "data1" }, new int[]
				{ android.R.id.text1, android.R.id.text2 });

		// 下面的代码插入一个联系人
		ContentValues values = new ContentValues();

		Uri rawContactUri = getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactsId = ContentUris.parseId(rawContactUri);

		values.clear();

		values.put(StructuredName.RAW_CONTACT_ID, rawContactsId);

		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);

		values.put(StructuredName.DISPLAY_NAME, "Li Ning");
		getContentResolver().insert(Data.CONTENT_URI, values);
		// 插入电话
		values.clear();
		values.put(Phone.RAW_CONTACT_ID, rawContactsId);
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Phone.NUMBER, "999999");
		getContentResolver().insert(Data.CONTENT_URI, values);

		// 下面的代码修改上面插入的联系人
		cursor = getContentResolver().query(Data.CONTENT_URI, null,
				Phone.NUMBER + "=?", new String[]
				{ "999999" }, null);
		cursor.moveToFirst();
		String id = cursor.getString(cursor
				.getColumnIndex(Phone.RAW_CONTACT_ID));
		values.clear();
		values.put(StructuredName.DISPLAY_NAME, "Liu Ming");
		getContentResolver().update(Data.CONTENT_URI, values,
				Phone.RAW_CONTACT_ID + "=?", new String[]
				{ id });
		// 下面的代码删除联系人
		getContentResolver().delete(Data.CONTENT_URI, "display_name=?",
				new String[]
				{ "Li Ning" });
		/*
		 * Cursor cursor = getContentResolver().query(
		 * Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "contacts"),
		 * null, null, null, null); SimpleCursorAdapter simpleCursorAdapter =
		 * new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1,
		 * cursor, new String[] { "display_name"}, new int[] {
		 * android.R.id.text1});
		 */

		setListAdapter(simpleCursorAdapter);
	}
}