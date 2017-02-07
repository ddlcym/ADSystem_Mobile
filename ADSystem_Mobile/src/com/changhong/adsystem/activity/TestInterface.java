package com.changhong.adsystem.activity;

import com.changhong.adsystem.http.HttpRequest;
import com.changhong.adsystem_mobile.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author cym
 * @date 创建时间：2017年1月4日 下午1:14:08
 * @version 1.0
 * @parameter
 */
public class TestInterface extends Activity {

	Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:

				break;

			}

			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);
		HttpRequest.getInstance().getCommunityList(handle, "", 10);
	}

}
