package com.changhong.adsystem.activity;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.changhong.adsystem.model.CommunityInfor;
import com.changhong.adsystem_mobile.R;

public class CommunityFragment extends BaseFragment {

	ListView mStrategyList = null;
	RelativeLayout mSearchNotice = null;
	LinearLayout mSearchBox=null;
	EditText mSearchKey = null;
	CommunityAdapter mCommunityAdapter = null;	
	List<CommunityInfor> mCommunityInfors=null;
	
	private static final int STRATEGY_ADAPTER_UPDATE = 0;
	private static final int STRATEGY_SHOW_PROGRESSDIALOG = 1;
	private static final int STRATEGY_HIDE_PROGRESSDIALOG = 2;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_ad_community;
	}

	@Override
	protected void initViewAndEvent(View v ) {

		mStrategyList = (ListView) v.findViewById(R.id.community_list);
		mSearchNotice = (RelativeLayout) v.findViewById(R.id.search_notice);
		mSearchBox = (LinearLayout) v.findViewById(R.id.search_inputbox);
		mSearchKey = (EditText) v.findViewById(R.id.search_input);

		// 小区列表
		fillCommunityInfors();
		mCommunityAdapter = new CommunityAdapter(getActivity(), mCommunityInfors);
		mStrategyList.setAdapter(mCommunityAdapter);
		mStrategyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position >=0 && position < mCommunityInfors.size()){
					
					int communityID =mCommunityInfors.get(position).comID;
					// 进入广告策略页面
					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.contentLayout,new StrategyPatternFragment(communityID));
					transaction.addToBackStack(null);
					transaction.commit();					
				}
						 
			}
		});
		
		v.findViewById(R.id.search_log).setOnClickListener(this);
		v.findViewById(R.id.search_submit).setOnClickListener(this);

		uiHander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case STRATEGY_ADAPTER_UPDATE:
					mCommunityAdapter.updateList(null);
					break;
				case STRATEGY_SHOW_PROGRESSDIALOG:
					break;
				case STRATEGY_HIDE_PROGRESSDIALOG:
					hideProgressDialog();
					break;
				default:
					break;

				}
			}

		};
		// 请求默认小区数据
		searchStrategy("");
	}

	
	

	private  void fillCommunityInfors() {
		if(null == mCommunityInfors){
			mCommunityInfors=new ArrayList<CommunityInfor>();
		}
		mCommunityInfors.clear();
		
		for (int i = 0; i < 10; i++) {
			CommunityInfor community=new CommunityInfor();
			community.comID=i;
			community.comName="长虹智能"+i+"区";
			mCommunityInfors.add(community);
		}
		
	}

	/**
	 * 根据关键字搜索小区
	 * 
	 * @param key
	 */
	private void searchStrategy(String key) {
		showProgressDialog();
		uiHander.sendEmptyMessageDelayed(STRATEGY_HIDE_PROGRESSDIALOG, 5000);
	}


	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		
		case R.id.search_log:
			mSearchNotice.setVisibility(View.GONE);
			//显示输入对话框
			mSearchBox.setVisibility(View.VISIBLE);
			break;
		case R.id.search_submit:
			mSearchBox.setVisibility(View.GONE);
			mSearchNotice.setVisibility(View.VISIBLE);
			// 搜索小区
            String key=mSearchKey.getText().toString();
			searchStrategy(key);
			break;


		}
	}

	
}


