package cn.wxjia.gfmonitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wxjia.gfmonitor.service.SendToWebService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

	private ViewPager mainViewPager;
	private List<View> mainViewList;
	private View view1, view2, view3;

	private TextView textMain1;
	private TextView textMain2;
	private TextView textMain3;

	private Drawable message_selectedDrawable;
	private Drawable message_unselectedDrawable;
	private Drawable contacts_selectedDrawable;
	private Drawable contacts_unselectedDrawable;
	private Drawable setting_selectedDrawable;
	private Drawable setting_unselectedDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * int ret = connection.getResponseCode(); 我也不知道为什么 执行这句代码时 就要加上下面的代码
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		setContentView(R.layout.activity_main);
		getDeviceInformation();

		mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);

		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.main_1, null);
		view2 = inflater.inflate(R.layout.main_2, null);
		view3 = inflater.inflate(R.layout.main_3, null);

		mainViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mainViewList.add(view1);
		mainViewList.add(view2);
		mainViewList.add(view3);

		textMain1 = (TextView) findViewById(R.id.textMain1);
		textMain2 = (TextView) findViewById(R.id.textMain2);
		textMain3 = (TextView) findViewById(R.id.textMain3);

		textMain1.setOnClickListener(new MyOnClickListener());
		textMain2.setOnClickListener(new MyOnClickListener());
		textMain3.setOnClickListener(new MyOnClickListener());

		message_selectedDrawable = getResources().getDrawable(
				R.drawable.message_selected);
		message_unselectedDrawable = getResources().getDrawable(
				R.drawable.message_unselected);
		contacts_selectedDrawable = getResources().getDrawable(
				R.drawable.contacts_selected);
		contacts_unselectedDrawable = getResources().getDrawable(
				R.drawable.contacts_unselected);
		setting_selectedDrawable = getResources().getDrawable(
				R.drawable.setting_selected);
		setting_unselectedDrawable = getResources().getDrawable(
				R.drawable.setting_unselected);

		message_unselectedDrawable.setBounds(0, 0,
				message_unselectedDrawable.getMinimumWidth(),
				message_unselectedDrawable.getMinimumHeight());
		message_selectedDrawable.setBounds(0, 0,
				message_selectedDrawable.getMinimumWidth(),
				message_selectedDrawable.getMinimumHeight());
		contacts_unselectedDrawable.setBounds(0, 0,
				contacts_unselectedDrawable.getMinimumWidth(),
				contacts_unselectedDrawable.getMinimumHeight());
		contacts_selectedDrawable.setBounds(0, 0,
				contacts_selectedDrawable.getMinimumWidth(),
				contacts_selectedDrawable.getMinimumHeight());
		setting_unselectedDrawable.setBounds(0, 0,
				setting_unselectedDrawable.getMinimumWidth(),
				setting_unselectedDrawable.getMinimumHeight());
		setting_selectedDrawable.setBounds(0, 0,
				setting_selectedDrawable.getMinimumWidth(),
				setting_selectedDrawable.getMinimumHeight());

		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return mainViewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mainViewList.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(mainViewList.get(position));
				return mainViewList.get(position);
			}

		};
		mainViewPager.setAdapter(pagerAdapter);

		mainViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				switch (index) {
				case 0:
					mainViewPager.setCurrentItem(0, true);
					clearTextMain();
					textMain1.setCompoundDrawables(null,
							message_selectedDrawable, null, null);
					textMain1.setTextColor(Color.WHITE);
					break;
				case 1:
					mainViewPager.setCurrentItem(1, true);
					clearTextMain();
					textMain2.setCompoundDrawables(null,
							contacts_selectedDrawable, null, null);
					textMain2.setTextColor(Color.WHITE);
					break;
				case 2:
					mainViewPager.setCurrentItem(2, true);
					clearTextMain();
					textMain3.setCompoundDrawables(null,
							setting_selectedDrawable, null, null);
					textMain3.setTextColor(Color.WHITE);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// Toast.makeText(getApplicationContext(), "onPageScrolled",
				// Toast.LENGTH_LONG).show();
			}

			@Override
			public void onPageScrollStateChanged(int index) {
				// Toast.makeText(getApplicationContext(),
				// "onPageScrollStateChanged = " + index,
				// Toast.LENGTH_LONG).show();
			}
		});

	}

	private final class MyOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.textMain1:
				mainViewPager.setCurrentItem(0, true);
				clearTextMain();
				textMain1.setCompoundDrawables(null, message_selectedDrawable,
						null, null);
				textMain1.setTextColor(Color.WHITE);
				break;
			case R.id.textMain2:
				mainViewPager.setCurrentItem(1, true);
				clearTextMain();
				textMain2.setCompoundDrawables(null, contacts_selectedDrawable,
						null, null);
				textMain2.setTextColor(Color.WHITE);
				break;
			case R.id.textMain3:
				mainViewPager.setCurrentItem(2, true);
				clearTextMain();
				textMain3.setCompoundDrawables(null, setting_selectedDrawable,
						null, null);
				textMain3.setTextColor(Color.WHITE);
				break;
			default:
				break;
			}
		}

	};

	public void clearTextMain() {
		textMain1.setTextColor(Color.parseColor("#82858b"));
		textMain2.setTextColor(Color.parseColor("#82858b"));
		textMain3.setTextColor(Color.parseColor("#82858b"));

		textMain1.setCompoundDrawables(null, message_unselectedDrawable, null,
				null);
		textMain2.setCompoundDrawables(null, contacts_unselectedDrawable, null,
				null);
		textMain3.setCompoundDrawables(null, setting_unselectedDrawable, null,
				null);
	}

	/**
	 * 获取硬件信息
	 */
	public void getDeviceInformation() {
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 手机唯一标识
		String imei = telephonyManager.getDeviceId();// 获取成功 866872013852384
		// 卡唯一标识
		String imsi = telephonyManager.getSubscriberId();// 获取失败
		// 手机型号
		String mType = android.os.Build.MODEL; // 获取成功 HUAWEI U8951
		String codename = android.os.Build.VERSION.CODENAME;// 获取成功 REL
		String teleNumber = telephonyManager.getLine1Number();// 获取失败

		String providersName = null;
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
			providersName = "中国移动";
		} else if (imsi.startsWith("46001")) {
			providersName = "中国联通";
		} else if (imsi.startsWith("46003")) {
			providersName = "中国电信";
		}

		String apps = null;
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				apps += i.applicationInfo.loadLabel(getPackageManager())
						.toString() + ",";
			}
		}

		// 获取总内存和 剩余内存
		String[] result = { "", "" }; // 1-total 2-avail

		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		long mAvailMem = mi.availMem;

		long mTotalMem = 0;
		String str1 = "/proc/meminfo";
		String str2 = null;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			mTotalMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
			localBufferedReader.close();
		} catch (IOException e) {
			Log.i(TAG, "IOException = " + e.getMessage());
		}
		result[0] = Formatter.formatFileSize(this, mTotalMem);
		result[1] = Formatter.formatFileSize(this, mAvailMem);

		str1 = "/proc/cpuinfo";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
			Log.i(TAG, "IOException = " + e.getMessage());
		}

		String macAddress = null;
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		macAddress = wifiInfo.getMacAddress();

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int mWidth = dm.widthPixels; // 宽
		int mHeight = dm.heightPixels; // 高

		Log.i(TAG, "imei = " + imei);
		Log.i(TAG, "mType = " + mType);
		Log.i(TAG, "codename = " + codename);
		Log.i(TAG, "teleNumber = " + teleNumber);
		Log.i(TAG, "imsi = " + imsi);
		Log.i(TAG, "providersName = " + providersName);
		Log.i(TAG, "apps = " + apps);
		Log.i(TAG, "meminfo total:" + result[0] + " used:" + result[1]);
		Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
		Log.i(TAG, "macAddress = " + macAddress);
		Log.i(TAG, "width = " + mWidth);
		Log.i(TAG, "height = " + mHeight);

		Map<String, String> params = new HashMap<String, String>();

		params.put("teleNumber", teleNumber);
		params.put("ip", "ip");
		params.put("address", "address");
		params.put("mType", mType);
		params.put("mWidth", String.valueOf(mWidth));
		params.put("mHeight", String.valueOf(mHeight));
		params.put("imei", imei);
		params.put("macAddress", macAddress);

		SendToWebService service = new SendToWebService();
		String path = "http://www.vcyoung.cn/tecentWeb/DeviceInformationServlet";
		service.sendPOSTRequest(path, params, "UTF-8");

	}
}
