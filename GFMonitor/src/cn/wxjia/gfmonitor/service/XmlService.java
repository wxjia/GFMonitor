package cn.wxjia.gfmonitor.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class XmlService {
	private static final String TAG = "XmlService";

	public InputStream getXmlImputStream(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			if (connection.getResponseCode() != 200) {
				return null;
			}
			InputStream inputStream = connection.getInputStream();
			return inputStream;
		} catch (IOException e) {
			Log.i(TAG, "IOException = " + e.getMessage());
		}
		return null;
	}
}