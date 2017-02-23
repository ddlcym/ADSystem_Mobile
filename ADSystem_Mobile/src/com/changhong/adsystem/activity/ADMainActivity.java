package com.changhong.adsystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.changhong.adsystem.http.VolleyTool;
import com.changhong.adsystem.nanohttpd.HTTPDService;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem.p2p.SendUDP;
import com.changhong.adsystem.p2p.UDPQuerySingleThread;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem_mobile.R;

public class ADMainActivity extends FragmentActivity {

	private VolleyTool volleyTool;
	private RequestQueue mReQueue;
	// 定义FragmentTabHost对象
	private FragmentTabHost mTabHost;
	// 定义数组来存放Fragment界面
	private final Class[] fragmentArray = { CommunityFragment.class,
			DeviceManagerF.class };
	private Intent onHomeIntent; // home键退出后通过intent启动程序

	private IntentFilter intentFilter;//wifi广播注册
	
	private boolean udpQureyFlag=false;//发送中则false;空闲中则为true

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Configure.UDPQueryTimeOut:
				udpQureyFlag=true;
				Toast.makeText(ADMainActivity.this, "未发现设备", Toast.LENGTH_SHORT)
						.show();
				P2PService.creatP2PService().close();
				break;
			case Configure.UDPQuerySuccess:
				udpQureyFlag=true;
				P2PService.creatP2PService().creatTcpConnect();
				break;
			case Configure.WifiClosed:
				Log.i("mmmm", "ADMainActivity-wificlosed");
				P2PService.creatP2PService().close();
				break;
			case Configure.WifiConnect:
				Log.i("mmmm", "ADMainActivity-WifiConnect");
				sendUDPQurey();
				break;

			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ad_main);
		initViewAndEvent();
		sendUDPQurey();
		registerBroadcast();//注册WiFi广播
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

	public void hideTabHost() {
		mTabHost.getTabWidget().setVisibility(View.GONE);
	}

	public void showTabHost() {
		mTabHost.getTabWidget().setVisibility(View.VISIBLE);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 拦截Intent，保存Intent，在onResume中进行处理
		onHomeIntent = intent;
	}

	@Override
	public void onResume() {

		if (onHomeIntent != null) { // home键退出后通过intent启动程序
			// dosomething···
			onHomeIntent = null;
		}
		super.onResume();
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterBroadcast();
	}

	private void sendUDPQurey() {
		if(!udpQureyFlag){
			udpQureyFlag=!udpQureyFlag;
			new UDPQuerySingleThread(handler).start();
		}
	}

	private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
				// signal strength changed
			} else if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上路由器与否
				System.out.println("网络状态改变");
				NetworkInfo info = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
					System.out.println("wifi网络连接断开");
					handler.sendEmptyMessage(Configure.WifiClosed);
				} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
					handler.sendEmptyMessage(Configure.WifiConnect);
					WifiManager wifiManager = (WifiManager) context
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();

					// 获取当前wifi名称
					System.out.println("连接到网络 " + wifiInfo.getSSID());

				}

			} else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_CHANGED_ACTION)) {// wifi打开与否，打开未必连接路由器
				int wifistate = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_DISABLED);

				if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
					System.out.println("系统关闭wifi");
				} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
					System.out.println("系统开启wifi");
				}
			}
		}
	};

	private void registerBroadcast() {
		if (intentFilter == null) {
			intentFilter = new IntentFilter();// 创建Intent对象
			intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		}
		getApplicationContext().registerReceiver(wifiReceiver, intentFilter);// 注册BroadcastReceiver
	}

	public void unregisterBroadcast() {
		if (intentFilter != null) {
			getApplicationContext().unregisterReceiver(wifiReceiver);// 取消注册Broadcast
																		// Receiver
			wifiReceiver = null;
			intentFilter = null;
		}
	}

}
