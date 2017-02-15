package com.changhong.adsystem.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import com.changhong.adsystem_mobile.R;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.changhong.adsystem.p2p.TCPClient;
import com.changhong.adsystem.p2p.UDPQuerySingleThread;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;

public class DeviceManagerF extends BaseFragment {

	private TextView hardwareVer;
	private TextView softwareVer;
	private TextView memoryTotal;
	private TextView memoryAvailable;
	private TextView stb_mac;
	private TextView adResourceID;
	private TextView appVersion;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case Configure.TCP_DEVICES_INFO:
				
				break;
			
			}
			
		}
		
	};
	
	@Override
	protected int getLayoutId() {
		return R.layout.device_infor;
	}

	@Override
	protected void initViewAndEvent(View v ) {
		hardwareVer=(TextView)v.findViewById(R.id.hardware_info);
		softwareVer=(TextView)v.findViewById(R.id.software_info);
		memoryTotal=(TextView)v.findViewById(R.id.memory_total);
		memoryAvailable=(TextView)v.findViewById(R.id.memory_availabe);
		stb_mac=(TextView)v.findViewById(R.id.mac);
		adResourceID=(TextView)v.findViewById(R.id.ad_resource_id);
		appVersion=(TextView)v.findViewById(R.id.app_version);
	}


	@Override
	public void onClick(View v) {
		
		
	}


	
	private void getDeviceInfo(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				queryDeviceInfo();
			}
		}).start();
	}
	
	
	private void queryDeviceInfo(){
		TCPClient tcpClient = TCPClient.instance();
		String result = tcpClient.sendMessage(queryDeviceInfoParams());
		Message respondMsg = handler.obtainMessage();
		respondMsg.what = Configure.TCP_DEVICES_INFO;
		respondMsg.obj = result;
		handler.sendMessage(respondMsg);
	}
	
	private String queryDeviceInfoParams(){
		JSONObject obj=new JSONObject();
		try {
			JSONObject json = new JSONObject();
			obj.put("action","getSTBInfo");
			obj.put("request", json);			
		} catch (JSONException e) {
			e.printStackTrace();
		}			
	  return obj.toString();
	}
}


