package com.changhong.adsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.changhong.adsystem.http.VolleyTool;
import com.changhong.adsystem_mobile.R;

public class ADMainActivity extends FragmentActivity {

	private VolleyTool volleyTool;
	private RequestQueue mReQueue;
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	// 定义数组来存放Fragment界面
	private final  Class[] fragmentArray = { CommunityFragment.class,DeviceManagerF.class };
	private Intent onHomeIntent; // home键退出后通过intent启动程序

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ad_main);
		initViewAndEvent();
	}

	protected void initViewAndEvent() {
		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.contentLayout);

		String[] tabs = getResources().getStringArray(R.array.tabs);
		for (int i = 0; i < tabs.length; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(tabs[i]).setIndicator(
					getTabItemView(tabs[i]));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			// 设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.tab_selector);
		}
		mTabHost.setCurrentTab(0);
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(String txt) {
		View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(txt);
		return view;
	}

    public void hideTabHost(){
    	mTabHost.getTabWidget().setVisibility(View.GONE);
    }
    
    public void showTabHost(){
    	mTabHost.getTabWidget().setVisibility(View.VISIBLE);
    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
}
