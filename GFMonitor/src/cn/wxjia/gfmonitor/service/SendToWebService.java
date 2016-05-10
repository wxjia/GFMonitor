package cn.wxjia.gfmonitor.service;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import android.util.Log;

public class SendToWebService {
	private static final String TAG = "WXJIA_MONITOR";

	/**
	 * 发送POST请求
	 * 
	 * @param path
	 * @param params
	 * @param encoding
	 * @return
	 */
	public boolean sendPOSTRequest(String path, Map<String, String> params,
			String encoding) {
		StringBuilder data = new StringBuilder();
		try {
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					data.append(entry.getKey()).append("=");
					data.append(URLEncoder.encode(entry.getValue(), encoding));
					data.append("&");
				}
				data.deleteCharAt(data.length() - 1);
			}
			Log.i(TAG, "data = " + data);
			byte[] entity = data.toString().getBytes();// 生成实体数据
			HttpURLConnection conn = (HttpURLConnection) new URL(path)
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);// 允许对外输出数据
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length",
					String.valueOf(entity.length));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(entity);
			if (conn.getResponseCode() == 200) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			Log.i(TAG, "UnsupportedEncodingException = " + e.getMessage());
		} catch (MalformedURLException e) {
			Log.i(TAG, "MalformedURLException = " + e.getMessage());
		} catch (ProtocolException e) {
			Log.i(TAG, "ProtocolException = " + e.getMessage());
		} catch (IOException e) {
			Log.i(TAG, "IOException = " + e.getMessage());
		} catch (Exception e) {
			Log.i(TAG, "Exception = " + e.getMessage());
		}
		return false;
	}
}
