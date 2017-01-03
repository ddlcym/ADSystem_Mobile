package com.changhong.adsystem.activity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.changhong.adsystem.http.RequestParams;
import com.changhong.adsystem.http.RequestURL;
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

	

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		super.initData();
		
	}

}
