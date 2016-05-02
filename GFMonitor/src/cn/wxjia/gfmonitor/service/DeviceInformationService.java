package cn.wxjia.gfmonitor.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import cn.wxjia.gfmonitor.MainActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

/**
 * 获取基本的手机硬件信息类
 * 
 * @author Jia_Vc
 * 
 */
public class DeviceInformationService {
	private TelephonyManager telephonyManager;
	private MainActivity activity;

	public DeviceInformationService(MainActivity activity) {
		this.activity = activity;
		telephonyManager = (TelephonyManager) activity
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取手机唯一标识imei
	 * 
	 * @return
	 */
	public String getDeviceId() {
		String imei = telephonyManager.getDeviceId();// 获取成功 866872013852384
		return imei;
	}

	/**
	 * 获取卡唯一标识imsi
	 * 
	 * @return
	 */
	public String getSubscriberId() {
		String imsi = telephonyManager.getSubscriberId();
		return imsi;
	}

	/**
	 * 获取供应商名字
	 * 
	 * @return
	 */
	public String getProvidersName() {
		String providersName = null;
		String imsi = telephonyManager.getSubscriberId();
		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
		if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
			providersName = "中国移动";
		} else if (imsi.startsWith("46001")) {
			providersName = "中国联通";
		} else if (imsi.startsWith("46003")) {
			providersName = "中国电信";
		}
		return providersName;
	}

	/**
	 * 获取已安装的应用名
	 * 
	 * @return
	 */
	public String getApps() {
		String apps = null;
		List<PackageInfo> packages = activity.getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				apps += i.applicationInfo.loadLabel(
						activity.getPackageManager()).toString()
						+ ",";
			}
		}
		return apps;
	}

	/**
	 * 获取内存信息 总内存和可用内存
	 * 
	 * @return
	 */
	public String[] getMemory() {
		String[] memory = new String[2];

		ActivityManager am = (ActivityManager) activity
				.getSystemService(Context.ACTIVITY_SERVICE);
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
		}
		memory[0] = Formatter.formatFileSize(activity, mTotalMem);
		memory[1] = Formatter.formatFileSize(activity, mAvailMem);
		return memory;
	}

	/**
	 * 获取CPU 型号和主频信息
	 * 
	 * @return
	 */
	public String[] getCpu() {
		String str1 = "/proc/cpuinfo";
		String[] cpuInfo = new String[2];
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			String str2 = localBufferedReader.readLine();
			String[] arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cpuInfo;
	}

	/**
	 * 获取Mac地址
	 * 
	 * @return
	 */
	public String getMacAddress() {
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	/**
	 * 获取屏幕的宽和高
	 * 
	 * @return
	 */
	public int[] getScreen() {
		int[] screen = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screen[0] = dm.widthPixels; // 宽
		screen[1] = dm.heightPixels; // 高
		return screen;
	}

	/**
	 * 返回经纬度
	 * 
	 * @return
	 */
	public double[] getCoordinate() {
		double[] coordinate = new double[2];
		LocationManager locationManager = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				coordinate[0] = location.getLongitude();
				coordinate[1] = location.getLatitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				@Override
				public void onLocationChanged(Location arg0) {

				}

				@Override
				public void onProviderDisabled(String arg0) {

				}

				@Override
				public void onProviderEnabled(String arg0) {

				}

				@Override
				public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				}
			};
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 1000, 10,
					locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				coordinate[0] = location.getLongitude(); // 经度
				coordinate[1] = location.getLatitude(); // 纬度
			}
		}
		return coordinate;
	}

	/**
	 * 获取WiFi的内网ip
	 * 
	 * @param activity
	 * @return
	 */
	public String getIp() {
		WifiManager wifiManager = (WifiManager) activity
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return intToIp(ipAddress);
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
}
