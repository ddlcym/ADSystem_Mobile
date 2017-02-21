package com.changhong.adsystem.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import com.changhong.adsystem.http.HttpRequest;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem_mobile.R;

public abstract class BaseFragment extends Fragment implements OnClickListener {

	protected Handler uiHander = null;
	protected ProgressDialog mProgressDialog = null;
    protected View mRootView=null;
    protected Activity mActivity =null; 
    protected HttpRequest mHttpRequest=null;
    protected P2PService  mP2PService=null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	this.mActivity=getActivity();
    	mHttpRequest = HttpRequest.getInstance();
    	mP2PService = P2PService.creatP2PService();
    	mP2PService.creatTcpConnect();
	}



	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }
        initViewAndEvent(mRootView);
        return mRootView;
    }
	
    
	
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}



	@Override
	public void onResume() {
        matchTabHost(getLayoutId());
		super.onResume();
	}


	public void showToast(String notice){
		Toast.makeText(mActivity, notice, Toast.LENGTH_SHORT).show();		
	}
	
	public void showToast(int resId){
		Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();		
	}
	

	/**
	 * 按键响应
	 * 
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	// 显示通讯等待进度条
	protected void showProgressDialog() {
		if (null == mProgressDialog) {
			mProgressDialog = new ProgressDialog(getActivity());
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置样式为圆形样式
			mProgressDialog.setMessage(getResources().getString(
					R.string.ab_progressdialog_notice));// 设置进度条的提示信息
			mProgressDialog.setIcon(R.drawable.ic_launcher); // 设置进度条的图标
			mProgressDialog.show();
		}
	}

	/**
	 * 隐藏进度条
	 */
	protected void hideProgressDialog() {
		if (null != mProgressDialog) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	private void matchTabHost(int layoutID){
	
		if(-1 == layoutID)return;		
		if (mActivity instanceof ADMainActivity) {
			if(R.layout.fragment_ad_strategy == layoutID){
			   ((ADMainActivity)mActivity).hideTabHost();
			}else{
			   ((ADMainActivity)mActivity).showTabHost();	
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	
	/**
	 * 
	 * 
	 *********************************************抽象方法定义******************************************************/
	protected abstract void initViewAndEvent(View v);
	protected abstract int getLayoutId();

}
