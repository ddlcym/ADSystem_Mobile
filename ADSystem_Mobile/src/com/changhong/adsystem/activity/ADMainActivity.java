package com.changhong.adsystem.activity;

import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.changhong.adsystem.http.VolleyTool;
import com.changhong.adsystem_mobile.R;

public class ADMainActivity extends BaseActivity {
	
	private VolleyTool volleyTool;
	private RequestQueue mReQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_main);
	}
	
	
//	private void getChannelTypes(){
//		String URL = processData.getTypes();
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
//				new Response.Listener<org.json.JSONObject>() {
//
//					@Override
//					public void onResponse(org.json.JSONObject arg0) {
//						// TODO Auto-generated method stub
////						Log.i("mmmm", "MainActivity***getChannelTypes:" + arg0);
//						channelTypes=HandleLiveData.getInstance().dealChannelTypes(arg0);
//						if(channelTypes!=null){
//							for(int i=0;i<channelTypes.size();i++){
//								ChannelType type=channelTypes.get(i);
//								List<ChannelInfo> list=new ArrayList<ChannelInfo>();
//								allCategeChanels.add(list);
//								allChanelsMap.put(type.getPramKey(), list);
//								}
//							}
//						}
//					}, errorListener);
//		jsonObjectRequest.setTag(ADMainActivity.class.getSimpleName());// 设置tag,cancelAll的时候使用
//		mReQueue.add(jsonObjectRequest);
//	}


	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		volleyTool = VolleyTool.getInstance();
		mReQueue = volleyTool.getRequestQueue();
	}
	
	


}
