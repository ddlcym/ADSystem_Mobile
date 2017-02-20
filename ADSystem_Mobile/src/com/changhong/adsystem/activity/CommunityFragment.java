package com.changhong.adsystem.activity;

import java.io.Serializable;
import java.util.List;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.changhong.adsystem.adapter.CommunityAdapter;
import com.changhong.adsystem.model.Class_Constant;
import com.changhong.adsystem.model.CommunityInfor;
import com.changhong.adsystem.model.JsonResolve;
import com.changhong.adsystem.utils.AesUtils;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;

public class CommunityFragment extends BaseFragment {

	protected static final String Tag = "CommunityFragment";

	// View定义
	ListView mCommunityList = null;
	RelativeLayout mSearchNotice = null;
	LinearLayout mSearchBox = null, mRefreshBox;
	EditText mSearchKey = null;
   
	//小区列表显示适配器
	CommunityAdapter mCommunityAdapter = null;
	List<CommunityInfor> mCommunityInfors = null;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_ad_community;
	}

	@Override
	protected void initViewAndEvent(View v) {

		mCommunityList = (ListView) v.findViewById(R.id.community_list);
		mSearchNotice = (RelativeLayout) v.findViewById(R.id.search_notice);
		mSearchBox = (LinearLayout) v.findViewById(R.id.search_inputbox);
		mRefreshBox = (LinearLayout) v.findViewById(R.id.refresh_box);
		mSearchKey = (EditText) v.findViewById(R.id.search_input);

		// 小区列表
		mCommunityAdapter = new CommunityAdapter(getActivity(),	mCommunityInfors);
		mCommunityList.setAdapter(mCommunityAdapter);
		mCommunityList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 0 && position < mCommunityInfors.size()) {

					Log.i(Tag,
							">>>>>>>>>>>>>>>>>>>>>>>>>>> start to call ADDetail>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
					List devList = mCommunityInfors.get(position).devList;
					if(null == devList){
						Toast.makeText(mActivity, R.string.ab_community_nodevlist, Toast.LENGTH_SHORT).show();
						return;
					}
					// 进入广告策略页面
//					FragmentTransaction transaction = getFragmentManager()
//							.beginTransaction();
//					transaction.replace(R.id.contentLayout,
//							new StrategyPatternFragment(devList));
//					transaction.addToBackStack(null);
//					transaction.commit();
					Intent intent=new Intent(getActivity(),StrategyPatternActivity.class);
					intent.putExtra("deviceInfors", (Serializable)devList);
					startActivity(intent);
				}

			}
		});

		v.findViewById(R.id.search_log).setOnClickListener(this);
		v.findViewById(R.id.search_submit).setOnClickListener(this);
		v.findViewById(R.id.refresh_btn).setOnClickListener(this);

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
					if (1000 == status) {
						mCommunityInfors = JsonResolve.getComunnitys(AesUtils
								.fixDecrypt(respond));
						mCommunityAdapter.updateList(mCommunityInfors);
					}
					doReFreshNitoce();

					break;
				case Class_Constant.POST_HIDE_PROGRESSDIALOG:
					hideProgressDialog();
					doReFreshNitoce();
					break;
				default:
					break;

				}
			}

		};
		// 请求默认小区数据
		searchCommunity("");
	}

	/**
	 * 根据关键字搜索小区
	 * 
	 * @param key
	 *            ""=默认搜索所有
	 */
	private void searchCommunity(String key) {
		showProgressDialog();
		mHttpRequest.getCommunityList(uiHander, key, 50);
		uiHander.sendEmptyMessageDelayed(
				Class_Constant.POST_HIDE_PROGRESSDIALOG,
				ServiceConfig.HTTP_MAX_WATING_TIME);
	}

	
	/**
	 * 根据列表信息适配显示刷新提示信息
	 */
	private void doReFreshNitoce(){
		if (null == mCommunityInfors
				|| 0 == mCommunityInfors.size()) {
			mRefreshBox.setVisibility(View.VISIBLE);
			mCommunityList.setVisibility(View.GONE);
		}else{
			mRefreshBox.setVisibility(View.GONE);
			mCommunityList.setVisibility(View.VISIBLE);
		}
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.search_log:
			mSearchNotice.setVisibility(View.GONE);
			// 显示输入对话框
			mSearchBox.setVisibility(View.VISIBLE);
			break;
		case R.id.search_submit:
			mSearchBox.setVisibility(View.GONE);
			mSearchNotice.setVisibility(View.VISIBLE);
			// 搜索小区
			String key = mSearchKey.getText().toString();
			searchCommunity(key);
			break;
		case R.id.refresh_btn:// 刷新
			// 搜索小区
			key = mSearchKey.getText().toString();
			searchCommunity(key);		
			break;
		}

	}

}
