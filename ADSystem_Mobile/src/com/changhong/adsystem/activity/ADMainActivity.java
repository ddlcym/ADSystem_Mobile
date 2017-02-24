package com.changhong.adsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.changhong.adsystem.nanohttpd.HTTPDService;
import com.changhong.adsystem.service.ADService;
import com.changhong.adsystem_mobile.R;


public class ADMainActivity extends FragmentActivity {

	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	// 定义数组来存放Fragment界面
	private static  Class[] fragmentArray = { CommunityFragment.class,
			DeviceManagerF.class };
	private Intent onHomeIntent; // home键退出后通过intent启动程序

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ad_main);
		initViewAndEvent();
		
		
		//启动UDP查询和WiFi监听service
		Intent serIntent=new Intent(this,ADService.class);
		startService(serIntent);
		
		
		
		/**
		 * 启动Http服务
		 */
		Intent http = new Intent(this, HTTPDService.class);
		startService(http);
	}

	protected void initViewAndEvent() {
		// 实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.contentLayout);

		String[] tabs = getResources().getStringArray(R.array.tabs);
		for (int i = 0; i < tabs.length; i++) {
			// 为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(tabs[i]).setIndicator(getTabItemView(tabs[i]));
			// 将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);			
		}     	
	}

	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(String txt) {
		View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
		TextView textView = (TextView) view.findViewById(R.id.textview);
		textView.setText(txt);
		// 设置Tab按钮的背景
		view.setBackgroundResource(R.drawable.tab_selector);
		return view;
	}

	public void hideTabHost() {
		mTabHost.getTabWidget().setVisibility(View.GONE);
	}

	public void showTabHost() {
		mTabHost.getTabWidget().setVisibility(View.VISIBLE);
	}


	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	

	

}
