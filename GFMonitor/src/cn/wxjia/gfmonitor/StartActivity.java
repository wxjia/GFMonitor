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
	private ViewPager startViewPager; // ��Ӧ��viewPager

	private List<View> startViewList;// view����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ʾ����֮ǰ�޸Ĵ��ڵ�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_start);

		startViewPager = (ViewPager) findViewById(R.id.startviewpager);
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.start_1, null);
		view2 = inflater.inflate(R.layout.start_2, null);
		view3 = inflater.inflate(R.layout.start_3, null);

		startViewList = new ArrayList<View>();// ��Ҫ��ҳ��ʾ��Viewװ��������
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
		Toast.makeText(getApplicationContext(), "����Ӧ��", Toast.LENGTH_LONG)
				.show();
		// ��ʽ��ͼ ��ȷָ�����������
		Intent intent = new Intent();
		// ����Ҫ��������
		intent.setClass(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
