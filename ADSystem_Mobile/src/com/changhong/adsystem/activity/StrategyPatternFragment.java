package com.changhong.adsystem.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.changhong.adsystem.model.AdStrategyPattern;
import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.model.DeviceInfor;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.AesUtils;
import com.changhong.adsystem.utils.Configure;
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
					JSONObject json = (JSONObject) msg.obj;
					int status = JsonResolve.getJsonObjInt(json, "status");
					String respond = JsonResolve.getJsonObjectString(json,
							"body");
					if (1000 == status) {
						mAdSPs = JsonResolve.getStrategyPatterns(AesUtils
								.fixDecrypt(respond));
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
		// 获取小区的广告策略
		String curMac = getDevMac();
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
		mHttpRequest.getStrategyPatternByMac(uiHander, mac);
		uiHander.sendEmptyMessageDelayed(
				Class_Constant.POST_HIDE_PROGRESSDIALOG,
				ServiceConfig.HTTP_MAX_WATING_TIME);
	}

	/**
	 * 获取当前设备的mac地址
	 * 
	 * @return
	 */
	private String getDevMac() {
		if (null != devList && curDevIndex < devList.size()) {
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
		case R.id.btn_commit:// 上传数据

			break;
		case R.id.btn_update:// 下载数据
			// searchStrategy(key);
			break;
		case R.id.btn_list:// 显示列表
			MyApplication.vibrator.vibrate(100);
			mdevSelectList.setVisibility(View.VISIBLE);
			break;

		}
	}

	/*
	 * 保存广告策略信息
	 */
	private void saveSPBaseFile(String spStr) {

		try {
			File spJson = new File(Configure.adBaseFilePath, "spJson.json");
			if (spJson.exists()) {
				spJson.delete();
			}
			FileWriter fw = new FileWriter(spJson);
			fw.write(spStr.toString(), 0, spStr.toString().length());
			fw.flush();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getSpBaseJson() {
		StringBuffer sb = new StringBuffer();
		File spJson = new File(Configure.adBaseFilePath + File.separator+ "spJson.json");
		if (spJson.exists()) {
			try {
				
				FileInputStream fis = new FileInputStream(spJson);
				byte[] buf = new byte[1024];
				while ((fis.read(buf)) != -1) {
					sb.append(new String(buf));
					buf = new byte[1024];// 重新生成，避免和上次读取的数据重复
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		return sb.toString();
	}

}
