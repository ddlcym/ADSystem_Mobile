package com.changhong.adsystem.activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.changhong.adsystem.model.DeviceInfor;
import com.changhong.adsystem.model.JsonPackage;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.p2p.P2PService;
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
	
	private DeviceInfor device=null;
	
	private  Handler handler=new Handler(){
		String result=null;
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what){
			case ServiceConfig.TCPS_ACTION_STBINFOR_CODE:
				result=(String) msg.obj;
				Log.i("mmmm", "DeviceManagerF-receive-devices-info:"+result);
				device=JsonResolve.resolveDeviceInfo(result);
				setData(device);
				
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
		getDeviceInfo();
		hardwareVer=(TextView)v.findViewById(R.id.hardware_info);
		softwareVer=(TextView)v.findViewById(R.id.software_info);
		memoryTotal=(TextView)v.findViewById(R.id.memory_total);
		memoryAvailable=(TextView)v.findViewById(R.id.memory_availabe);
		stb_mac=(TextView)v.findViewById(R.id.mac);
		adResourceID=(TextView)v.findViewById(R.id.ad_resource_id);
		appVersion=(TextView)v.findViewById(R.id.app_version);
		
	}

	private void setData(DeviceInfor device){
		long mLTotal=device.getMemoryTotal();
		long mLAvailable=device.getMemoryAvailable();
		String mStrTotal=mLTotal/1000000+" MB";
		String mStrAvailable=mLAvailable/1000000+" MB";
		
		
		hardwareVer.setText(device.getStbHardwareVersion());
		softwareVer.setText(device.getStbSoftwareVersion());
		memoryTotal.setText(mStrTotal);
		memoryAvailable.setText(mStrAvailable);
		stb_mac.setText(device.getMac());
		adResourceID.setText(device.getAdResouseId());
		appVersion.setText(device.getAppVersion()+"");
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
		String sendMsg=JsonPackage.queryDeviceInfoParams();
		P2PService.creatP2PService().communicationWithBox(handler, ServiceConfig.TCPS_ACTION_STBINFOR_CODE, "");
	}
	
	
}


