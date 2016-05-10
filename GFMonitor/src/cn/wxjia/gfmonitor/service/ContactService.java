package cn.wxjia.gfmonitor.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public class ContactService {

	public List<String> getContacts(Activity activity) {
		// 内容提供者表示 content://com.android.contacts
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		ContentResolver contentResolver = activity.getContentResolver();
		Cursor cursor = contentResolver.query(uri, new String[] { "_id" },
				null, null, null);
		List<String> contacts = new ArrayList<String>();
		while (cursor.moveToNext()) {
			int contactid = cursor.getInt(0);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contactid + "/data");
			Cursor cursor2 = contentResolver.query(uri, new String[] {
					"mimetype", "data1" }, null, null, null);
			String person = "";
			while (cursor2.moveToNext()) {
				String data = cursor2
						.getString(cursor2.getColumnIndex("data1"));
				String type = cursor2.getString(cursor2
						.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(type)) {// 姓名
					person = person + "name=" + data + "\t";
				} else if ("vnd.android.cursor.item/phone_v2".equals(type)) {// 电话
					person = person + "tel=" + data + "\t";
				} else if ("vnd.android.cursor.item/email_v2".equals(type)) {// email
					person = person + "email=" + data + "\t";
				}
			}
			contacts.add(person);
		}
		return contacts;
	}
}
