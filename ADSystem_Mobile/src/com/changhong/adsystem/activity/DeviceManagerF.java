package com.changhong.adsystem.activity;

import com.changhong.adsystem_mobile.R;

import android.view.View;
import android.widget.TextView;


public class DeviceManagerF extends BaseFragment {

	private TextView hardwareVer;
	private TextView softwareVer;
	private TextView memoryTotal;
	private TextView memoryAvailable;
	private TextView stb_mac;
	private TextView adResourceID;
	private TextView appVersion;
	
	
	
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




}


