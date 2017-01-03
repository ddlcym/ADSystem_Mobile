package com.changhong.adsystem.activity;

import android.os.Bundle;
import android.view.Window;

import com.changhong.adsystem_mobile.R;

public class StrategyPatternActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.activity_ad_strategy);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initView() {
		
	}
}
