package com.changhong.adsystem.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.changhong.adsystem.model.AdStrategyPattern;
import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.model.DeviceInfor;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.AesUtils;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem_mobile.R;

public class StrategyPatternFragment extends BaseFragment {
	
	//视图定义
	private ListView mStrategyList = null;
	private StrategyAdapter mStrategyAdapter = null;
	private List<AdStrategyPattern> mAdSPs =new ArrayList<AdStrategyPattern>();
	private List<DeviceInfor> devList;
	private int curDevIndex=0;

	public StrategyPatternFragment(List<DeviceInfor> devList){
		this.devList=devList;
		curDevIndex=0;
	}


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_ad_strategy;
	}

	@Override
	protected void initViewAndEvent(View v ) {
		mStrategyList = (ListView) v.findViewById(R.id.strategy_list);
		mStrategyAdapter = new StrategyAdapter(mActivity, mAdSPs);
		mStrategyList.setAdapter(mStrategyAdapter);
		mStrategyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 进入广告策略页面
			}
		});
	    
		v.findViewById(R.id.btn_update).setOnClickListener(this);
		v.findViewById(R.id.btn_commit).setOnClickListener(this);

		uiHander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Class_Constant.REQUEST_STRATEPATTERN:
					hideProgressDialog();
					JSONObject json = (JSONObject) msg.obj;
					int status = JsonResolve.getJsonObjInt(json, "status");
					String respond = JsonResolve.getJsonObjectString(json,"body");
					if (1000 == status) {
						mAdSPs = JsonResolve.getStrategyPatterns(AesUtils.fixDecrypt(respond));
						mStrategyAdapter.updateList(mAdSPs);
					}

					break;
				case Class_Constant.POST_HIDE_PROGRESSDIALOG:
					hideProgressDialog();
					break;
				default:
					break;

				}				
			}
		};
		//获取小区的广告策略
		String curMac=getDevMac();
		if(!curMac.equals("")){
		    requestStrategy(curMac);
		}
	}



	/**
	 * 根据关键字搜索小区
	 * 
	 * @param key
	 */
	private void requestStrategy(String mac) {
		showProgressDialog();
		mHttpRequest.getStrategyPatternByMac(uiHander, mac);
		uiHander.sendEmptyMessageDelayed(
				Class_Constant.POST_HIDE_PROGRESSDIALOG,
				Configure.HTTP_MAX_WATING_TIME);
	}

	
	/**
	 * 获取当前设备的mac地址
	 * @return
	 */
	private String getDevMac(){
		if(null != devList && curDevIndex < devList.size()){
			return devList.get(curDevIndex).mac;
		}
		return "";
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit://上传数据
			//弹出输入对话框
			break;
		case R.id.btn_update://下载数据
//			searchStrategy(key);
			break;
		

		}
	}
}
