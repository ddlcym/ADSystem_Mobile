package com.changhong.adsystem.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.changhong.adsystem.adapter.DeviceSelectAdapter;
import com.changhong.adsystem.adapter.StrategyAdapter;
import com.changhong.adsystem.http.image.loader.core.ImageLoadController;
import com.changhong.adsystem.model.AdStrategyPattern;
import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.model.DeviceInfor;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.AesUtils;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.FileUtil;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;
import com.changhong.common.system.MyApplication;

public class StrategyPatternFragment extends BaseFragment {

	// 视图定义
	private ListView mStrategyList = null, mdevSelectList = null;
	private TextView focusDev = null;
	private StrategyAdapter mStrategyAdapter = null;
	private DeviceSelectAdapter mDeviceSelectAdapter = null;
	private List<AdStrategyPattern> mAdSPs = new ArrayList<AdStrategyPattern>();
	private List<DeviceInfor> devList;
	private int curDevIndex = 0;

	public StrategyPatternFragment(List<DeviceInfor> devList) {
		this.devList = devList;
		this.curDevIndex = 0;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_ad_strategy;
	}

	@Override
	protected void initViewAndEvent(View v) {
		mStrategyList = (ListView) v.findViewById(R.id.strategy_list);
		mdevSelectList = (ListView) v.findViewById(R.id.dev_list);
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
		initDevlist(v);
		uiHander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Class_Constant.REQUEST_STRATEPATTERN:
					hideProgressDialog();
//					JSONObject jsonObj =JsonResolve.getJsonObj((String) msg.obj);
//                    if(null == jsonObj)break;
//					int status = JsonResolve.getJsonObjInt(jsonObj, "status");
//					String respond = JsonResolve.getJsonObjectString(jsonObj,"body");
//					if (1000 == status) {
//						respond=AesUtils.fixDecrypt(respond);
				    	String respond=(String) msg.obj;
						mAdSPs = JsonResolve.getStrategyPatterns(respond);					
						mStrategyAdapter.updateList(mAdSPs);
						new FileUtil().writeToSDCard(Configure.adBaseFilePath, respond);
//					}
					
					
					break;
				case Class_Constant.POST_HIDE_PROGRESSDIALOG:
					hideProgressDialog();
					break;
				case ServiceConfig.IMAGE_EXIST:
					break;
				case ServiceConfig.IMAGE_DOWNLOAD_FINISHED:
					break;
				case ServiceConfig.IMAGE_DOWNLOAD_FAILED:
					break;
				default:
					testJson();
					break;

				}
			}
		};
		// 获取小区的广告策略
		String curMac = getDeviceMac();
		if (!curMac.equals("")) {
			requestStrategy(curMac);
		}
	}

	private void initDevlist(View v) {
		focusDev = (TextView) v.findViewById(R.id.title);
		focusDev.setText("STB_" + devList.get(curDevIndex).mac);
		mDeviceSelectAdapter = new DeviceSelectAdapter(mActivity, devList);
		mdevSelectList.setAdapter(mDeviceSelectAdapter);
		mdevSelectList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mDeviceSelectAdapter.notifyDataSetChanged();
				curDevIndex = arg2;
				focusDev.setText(devList.get(curDevIndex).mac);
				mdevSelectList.setVisibility(View.GONE);
			}
		});
		v.findViewById(R.id.btn_list).setOnClickListener(this);

	}

	/**
	 * 根据关键字搜索小区
	 * 
	 * @param key
	 */
	private void requestStrategy(String mac) {
		showProgressDialog();
//		mHttpRequest.getStrategyPatternByMac(uiHander, mac);
//		uiHander.sendEmptyMessageDelayed(
//				Class_Constant.POST_HIDE_PROGRESSDIALOG,
//				ServiceConfig.HTTP_MAX_WATING_TIME);

		//测试用
		uiHander.sendEmptyMessageDelayed(255, 10000);
	}

	/**
	 * 获取当前设备的mac地址
	 * 
	 * @return
	 */
	private String getDeviceMac() {
		if (null != devList && curDevIndex < devList.size()) {
			return devList.get(curDevIndex).mac;
		}
		return "";
	}
	
	/**
	 * 下载广告资源文件
	 */
	private void downLoadADRes(){
		ImageLoadController imgCtr=ImageLoadController.getInstance();
		for(AdStrategyPattern adsp : mAdSPs){
			int size=adsp.urls.size();
			for (int i = 0; i < size; i++) {
				String imagePath=adsp.urls.get(i);
				if(!imgCtr.imageIsExist(imagePath)){
					imgCtr.gotoDownloadWay(uiHander, true, imagePath);
				}
			}			
		}		
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:// 上传数据
            
			break;
		case R.id.btn_update://下载广告资源文件	
			downLoadADRes();
			break;
		case R.id.btn_list:// 显示列表
			MyApplication.vibrator.vibrate(100);
			mdevSelectList.setVisibility(View.VISIBLE);
			break;

		}
	}
	
	private void testJson(){
		FileUtil fileUtil=new FileUtil();
		String spjson=fileUtil.readFileFromSDCard(Configure.adBaseFilePath+"/spJson.json");
		spjson=spjson.replace(" ", "");
		uiHander.sendMessage(uiHander.obtainMessage(Class_Constant.REQUEST_STRATEPATTERN, spjson));
	}

	
}
