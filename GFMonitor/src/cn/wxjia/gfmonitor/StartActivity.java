package cn.wxjia.gfmonitor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class StartActivity extends Activity {
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
	}

	public void enterMain(View view) {
		Toast.makeText(getApplicationContext(), "进入应用", Toast.LENGTH_LONG)
				.show();
		// 显式意图 明确指定了组件名称
		Intent intent = new Intent();
		// 设置要激活的组件
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
