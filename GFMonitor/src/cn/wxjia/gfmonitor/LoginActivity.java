package cn.wxjia.gfmonitor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wxjia.gfmonitor.service.ContactToWebService;
import cn.wxjia.gfmonitor.tool.StreamTool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private static final String TAG = "WXJIA_MONITOR";
	private EditText usernameText;
	private EditText passwordText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		/**
		 * int ret = connection.getResponseCode(); 我也不知道为什么 执行这句代码时 就要加上下面的代码
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		usernameText = (EditText) findViewById(R.id.username);
		passwordText = (EditText) findViewById(R.id.password);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle(R.string.exit)
					.setMessage(R.string.ensureExit)
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							})
					.setPositiveButton(R.string.ensure,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									finish();
								}
							}).show();
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		System.exit(0);
	}

	public void enterMain(View view) throws UnsupportedEncodingException,
			MalformedURLException, ProtocolException, IOException, Exception {
		if (null == usernameText
				|| usernameText.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "邮箱为空", Toast.LENGTH_LONG)
					.show();
			return;
		}

		String username = usernameText.getText().toString();
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

		Matcher matcher = pattern.matcher(username);
		if (!matcher.matches()) {
			Toast.makeText(getApplicationContext(), "邮箱格式不正确",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (null == passwordText
				|| passwordText.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "密码为空", Toast.LENGTH_LONG)
					.show();
			return;
		}
		if (username.length() > 50
				|| usernameText.getText().toString().length() > 50) {
			Toast.makeText(getApplicationContext(), "用户名或密码过长",
					Toast.LENGTH_LONG).show();
			return;
		}

		ContactToWebService contactToWebService = new ContactToWebService();
		// http://192.168.1.101/tecentWeb/AppLoginServlet?username=jiaweixi&password=jiaweixi
		// http://www.vcyoung.cn/tecentWeb/AppLoginServlet?username=jiaweixi&password=jiaweixi
		String path = "http://www.vcyoung.cn/tecentWeb/AppLoginServlet";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", passwordText.getText().toString());
		InputStream inputStream = contactToWebService.fetchRequest(path,
				params, "UTF-8");

		byte[] retBytes = StreamTool.getByteFromInputStream(inputStream);
		String retStrs = new String(retBytes);
		Log.i(TAG, "服务器返回 = " + retStrs);
		if ("NO".equals(retStrs)) {
			Toast.makeText(getApplicationContext(), "用户名或密码错误",
					Toast.LENGTH_LONG).show();
			return;
		} else if ("YES".equals(retStrs)) {
			// 显式意图 明确指定了组件名称
			Intent intent = new Intent();
			// 设置要激活的组件
			intent.setClass(getApplicationContext(), MainActivity.class);
			intent.putExtra("username", username);
			startActivity(intent);
		}

	}
}
