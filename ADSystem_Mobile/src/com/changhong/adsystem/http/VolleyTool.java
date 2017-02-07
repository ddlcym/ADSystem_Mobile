package com.changhong.adsystem.http;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.changhong.adsystem.activity.MyApp;
import com.changhong.common.system.MyApplication;



public class VolleyTool {

	private static VolleyTool volleyTool;
	
	private RequestQueue requestQueue;
	public static VolleyTool getInstance(){
		
		if(null==volleyTool){
			volleyTool=new VolleyTool();
		}
		return volleyTool;
	}
	
	public VolleyTool(){
		if(null==requestQueue){
			requestQueue=Volley.newRequestQueue(MyApplication.getContext());
		}
	}
	
	public RequestQueue getRequestQueue(){
		return requestQueue;
	}
	
	public void stop(Object tag){
		requestQueue.cancelAll(tag);
	}
}

