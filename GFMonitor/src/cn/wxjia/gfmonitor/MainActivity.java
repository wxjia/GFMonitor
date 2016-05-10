package cn.wxjia.gfmonitor;

import java.util.ArrayList;
import java.util.List;

import cn.wxjia.gfmonitor.service.SendLoginRecord;
import cn.wxjia.gfmonitor.service.UpdateManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "WXJIA_MONITOR";

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
		setContentView(R.layout.activity_main);

		/**
		 * int ret = connection.getResponseCode(); 我也不知道为什么 执行这句代码时 就要加上下面的代码
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
		textMain1 = (TextView) findViewById(R.id.textMain1);
		textMain2 = (TextView) findViewById(R.id.textMain2);
		textMain3 = (TextView) findViewById(R.id.textMain3);

		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.main_1, null);
		view2 = inflater.inflate(R.layout.main_2, null);
		view3 = inflater.inflate(R.layout.main_3, null);

		mainViewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		mainViewList.add(view1);
		mainViewList.add(view2);
		mainViewList.add(view3);

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
		// 插入登录记录
		boolean ret = new SendLoginRecord().sendLoginRecord(this);
		if (ret == false) {
			Toast.makeText(getApplicationContext(), "发送登录记录失败",
					Toast.LENGTH_LONG).show();
		}

		// 一切完成之后 进行版本更新检查
		versionCheck(null);
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

	public void versionCheck(View view) {
		UpdateManager updateManager = new UpdateManager(this);
		updateManager.checkUpdate();
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

}
