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

import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.AdDetail;
import com.changhong.adsystem.utils.AesUtils;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem_mobile.R;

public class StrategyPatternFragment extends BaseFragment {
	
	//视图定义
	ListView mStrategyList = null;
	StrategyAdapter mStrategyAdapter = null;

	private String communityID;
	public StrategyPatternFragment(String communityID){
		this.communityID=communityID;
	}


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_ad_strategy;
	}

	@Override
	protected void initViewAndEvent(View v ) {
		mStrategyList = (ListView) v.findViewById(R.id.strategy_list);
		// 小区列表
		List<AdDetail> strategyList = getStrategyList();
		mStrategyAdapter = new StrategyAdapter(mActivity, strategyList);
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
				case Class_Constant.REQUEST_COMMUNITY:
					hideProgressDialog();
					JSONObject json = (JSONObject) msg.obj;
					int status = JsonResolve.getJsonObjInt(json, "status");
					String respond = JsonResolve.getJsonObjectString(json,
							"body");
//					if (1000 == status) {
//						mCommunityInfors = JsonResolve.getComunnitys(AesUtils
//								.fixDecrypt(respond));
//						mCommunityAdapter.updateList(mCommunityInfors);
//					}

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
		requestStrategy(communityID);
	}

	private List<AdDetail> getStrategyList() {
		List<AdDetail> dataList = new ArrayList<AdDetail>();
		
		for (int j = 0; j < 10; j++) {		
			AdDetail oneAD=new AdDetail();
			oneAD.name="广告"+j;
			oneAD.adImagePath="data/ad/strategy/ad1";
			oneAD.startDate="2017-02-06";
			oneAD.endDate="2017-06-06";
			oneAD.adType=".png";
			oneAD.repeat="3";
			dataList.add(oneAD);
		}
		return dataList;
	}

	/**
	 * 根据关键字搜索小区
	 * 
	 * @param key
	 */
	private void requestStrategy(String communityID) {
		showProgressDialog();
		uiHander.sendEmptyMessageDelayed(
				Class_Constant.POST_HIDE_PROGRESSDIALOG,
				Configure.HTTP_MAX_WATING_TIME);
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
