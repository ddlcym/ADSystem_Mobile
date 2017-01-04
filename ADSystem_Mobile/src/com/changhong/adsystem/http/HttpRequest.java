package com.changhong.adsystem.http;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.changhong.adsystem.activity.LoginActivity;
import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.utils.L;

/**
 * @author cym
 * @date 创建时间：2016年12月20日 下午1:59:40
 * @version 1.0
 * @parameter
 */
public class HttpRequest {

	private VolleyTool volleyTool;
	private RequestQueue mReQueue;
	private static HttpRequest httpRequest;

	public HttpRequest() {
		if (null == volleyTool) {
			volleyTool = VolleyTool.getInstance();
		}
		if (null == mReQueue) {
			mReQueue = volleyTool.getRequestQueue();
		}
	}
	
	public static HttpRequest getInstance(){
		
		if(null==httpRequest){
			httpRequest=new HttpRequest();
		}
		return httpRequest;
	}

	public void postSecurityCode(final Handler handler,String mobile) {
		String URL = RequestURL.getSecurityCode(mobile);
		L.d(URL);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST, URL, null,
				new Response.Listener<org.json.JSONObject>() {

					@Override
					public void onResponse(org.json.JSONObject arg0) {
						// TODO Auto-generated method stub
						// Log.i("mmmm", "HttpRequest***postSecurityCode:" +
						// arg0);
						Message msg=new Message();
						msg.what=Class_Constant.POST_SECURITYCODE_RESULT;
						msg.obj=arg0;
						handler.sendMessage(msg);
					}
				}, errorListener);
		jsonObjectRequest.setTag(HttpRequest.class.getSimpleName());// 设置tag,cancelAll的时候使用
		mReQueue.add(jsonObjectRequest);
	}
	
	public void login(final Handler handler,String moblie,String securityCode){
		String URL = RequestURL.getLoginURL(moblie,securityCode);
		L.d(URL);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST, URL, null,
				new Response.Listener<org.json.JSONObject>() {

					@Override
					public void onResponse(org.json.JSONObject arg0) {
						// TODO Auto-generated method stub
						// Log.i("mmmm", "HttpRequest***postSecurityCode:" +
						// arg0);
						Message msg=new Message();
						msg.what=Class_Constant.POST_LOGIN;
						msg.obj=arg0;
						handler.sendMessage(msg);
					}
				}, errorListener);
		jsonObjectRequest.setTag(HttpRequest.class.getSimpleName());// 设置tag,cancelAll的时候使用
		mReQueue.add(jsonObjectRequest);
	}
	
	/*
	 * 根据关键字获取指定数量的小区列表
	 * 
	 * @param 
	 * words 
	 * 		the keywords of the residential
	 * 
	 * number 
	 * 		the number of residential 
	 */
	public void getResidential(final Handler handler,String words,int number){
		String URL = RequestURL.getResidential(words,number);
		L.d(URL);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
				Request.Method.POST, URL, null,
				new Response.Listener<org.json.JSONObject>() {

					@Override
					public void onResponse(org.json.JSONObject arg0) {
						// TODO Auto-generated method stub
						 Log.i("mmmm", "HttpRequest***getResidential:" +
						 arg0);
						Message msg=new Message();
						msg.what=Class_Constant.POST_LOGIN;
						msg.obj=arg0;
						handler.sendMessage(msg);
					}
				}, errorListener);
		jsonObjectRequest.setTag(HttpRequest.class.getSimpleName());// 设置tag,cancelAll的时候使用
		mReQueue.add(jsonObjectRequest);
		
	}

	private Response.ErrorListener errorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			Log.i("mmmm", "MainActivity=error：" + arg0);
		}
	};

}
