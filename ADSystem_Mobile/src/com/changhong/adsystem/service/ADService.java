package com.changhong.adsystem.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.changhong.adsystem.activity.MyApp;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem.p2p.UDPQuerySingleThread;
import com.changhong.adsystem.utils.Configure;

/**
 * @author cym
 * @date 创建时间：2017年2月23日 下午4:57:02
 * @version 1.0
 * @parameter
 */
public class ADService extends Service {

	private IntentFilter intentFilter;// wifi广播注册
	private boolean udpQureyFlag = false;// 发送中则false;空闲中则为true

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sendUDPQurey();
		registerBroadcast();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterBroadcast();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case Configure.UDPQueryTimeOut:
				Log.i("mmmm", "ADMainActivity-UDPQueryTimeOut");
				udpQureyFlag = true;
				Toast.makeText(MyApp.getContext(), "未发现设备", Toast.LENGTH_SHORT)
						.show();
				P2PService.creatP2PService().close();
				break;
			case Configure.UDPQuerySuccess:
				udpQureyFlag = true;
				Log.i("mmmm", "ADMainActivity-UDPQuerySuccess");
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
	private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.i("mmmm", "wifiReceiver:action="+intent.getAction());
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
					WifiManager.WIFI_STATE_DISABLED)) {

			} else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_ENABLED)) {

			}else if (intent.getAction().equals(
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
			intentFilter.addAction(WifiManager.EXTRA_WIFI_STATE);
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

	private void sendUDPQurey() {
		if (!udpQureyFlag) {
			udpQureyFlag = !udpQureyFlag;
			new UDPQuerySingleThread(handler).start();
		}
	}
}
