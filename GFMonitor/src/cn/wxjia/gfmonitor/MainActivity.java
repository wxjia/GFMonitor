package cn.wxjia.gfmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wxjia.gfmonitor.service.DeviceInformationService;
import cn.wxjia.gfmonitor.service.SendToWebService;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
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
			}

			@Override
			public void onPageScrollStateChanged(int index) {
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
		DeviceInformationService deviceService = new DeviceInformationService(
				this);
		TelephonyManager telephonyManager;
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 手机型号
		String mType = android.os.Build.MODEL; // 获取成功 HUAWEI U8951
		// 电话号码
		String teleNumber = telephonyManager.getLine1Number();// 获取失败

		String imei = deviceService.getDeviceId();
		String imsi = deviceService.getSubscriberId();
		String providersName = deviceService.getProvidersName();
		String apps = deviceService.getApps();
		String macAddress = deviceService.getMacAddress();
		String ip = deviceService.getIp();

		String[] memory = deviceService.getMemory();
		String[] cpuInfo = deviceService.getCpu();
		int[] screen = deviceService.getScreen();
		double[] coordinate = deviceService.getCoordinate();

		Log.i(TAG, "mType = " + mType);
		Log.i(TAG, "teleNumber = " + teleNumber);
		Log.i(TAG, "imsi = " + imsi);
		Log.i(TAG, "providersName = " + providersName);
		Log.i(TAG, "apps = " + apps);
		Log.i(TAG, "macAddress = " + macAddress);
		Log.i(TAG, "meminfo total:" + memory[0] + " available:" + memory[1]);
		Log.i(TAG, "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
		Log.i(TAG, "width = " + screen[0]);
		Log.i(TAG, "height = " + screen[1]);

		Log.i(TAG, "Longitude = " + coordinate[0]);
		Log.i(TAG, "Latitude = " + coordinate[1]);
		String address = "Latitude-" + coordinate[1] + "+Longitude-"
				+ coordinate[0];

		Map<String, String> params = new HashMap<String, String>();

		params.put("teleNumber", teleNumber);
		params.put("ip", ip);
		params.put("address", address);
		params.put("mType", mType);
		params.put("mWidth", String.valueOf(screen[0]));
		params.put("mHeight", String.valueOf(screen[1]));
		params.put("imei", imei);
		params.put("macAddress", macAddress);

		SendToWebService service = new SendToWebService();
		String path = "http://www.vcyoung.cn/tecentWeb/DeviceInformationServlet";
		service.sendPOSTRequest(path, params, "UTF-8");

	}

}
