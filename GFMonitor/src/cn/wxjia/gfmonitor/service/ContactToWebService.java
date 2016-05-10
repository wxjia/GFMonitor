package cn.wxjia.gfmonitor.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

public class ContactToWebService {
	private static final String TAG = "WXJIA_MONITOR";

	/**
	 * 发送POST请求
	 * 
	 * @param path
	 * @param params
	 * @param encoding
	 * @return
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	public InputStream fetchRequest(String path, Map<String, String> params,
			String encoding) throws UnsupportedEncodingException,
			MalformedURLException, ProtocolException, IOException, Exception {
		StringBuilder data = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				data.append(entry.getKey()).append("=");
				data.append(URLEncoder.encode(entry.getValue(), encoding));
				Log.i(TAG,
						"编码后的参数 = "
								+ URLEncoder.encode(entry.getValue(), encoding));
				data.append("&");
			}
			data.deleteCharAt(data.length() - 1);
		}
		byte[] entity = data.toString().getBytes();// 生成实体数据

		HttpURLConnection conn = (HttpURLConnection) new URL(path)
				.openConnection();
		Log.i(TAG, "path = " + path);
		Log.i(TAG, "实体数据 = " + data);
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);// 允许对外输出数据
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entity);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		return null;
	}
}
