package com.changhong.adsystem.service;

import com.android.volley.RequestQueue;
import com.changhong.adsystem.http.VolleyTool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** 
 * @author  cym  
 * @date 创建时间：2016年12月13日 下午2:12:10 
 * @version 1.0 
 * @parameter   
 */
public class LogReportService extends Service{

	private VolleyTool volleyTool;
	private RequestQueue mReQueue;

	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initData();
		
	}
	
	private void initData(){
		volleyTool = VolleyTool.getInstance();
		mReQueue = volleyTool.getRequestQueue();
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
	}
	
	
	

}
