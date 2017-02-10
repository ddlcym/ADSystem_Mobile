package com.changhong.adsystem.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.changhong.adsystem.model.AdStrategyPattern;
import com.changhong.adsystem.utils.FileUtil;
import com.changhong.adsystem_mobile.R;
import com.changhong.common.system.MyApplication;

public class StrategyAdapter extends BaseAdapter {

	private LayoutInflater minflater;
	private List<AdStrategyPattern> strategyList;
	private Resources res;
	private FileUtil mFileUtil=null;
	
	public StrategyAdapter(Context context, List<AdStrategyPattern> strategyList) {
		this.minflater = LayoutInflater.from(context);
		this.strategyList = strategyList;
		res=context.getResources();
		mFileUtil=new FileUtil();

	}

	/**
	 * 更新数据，更新list后在调用notifyDataSetChanged()
	 * 
	 * @param ipList
	 */
	public void updateList(List<AdStrategyPattern> strategyList) {
		if (null != strategyList) {
			this.strategyList = strategyList;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return strategyList == null ? 0 : strategyList.size();
	}

	@Override
	public Object getItem(int position) {
		return strategyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = minflater.inflate(R.layout.ad_strategy_item, null);
			vh.index = (TextView) convertView.findViewById(R.id.ad_index);
			vh.adLog = (ImageView) convertView.findViewById(R.id.ad_log);
			vh.name = (TextView) convertView.findViewById(R.id.ad_name);
			vh.fileNum = (TextView) convertView.findViewById(R.id.ad_filenum);
			vh.endDate = (TextView) convertView.findViewById(R.id.ad_enddate);
			vh.adType = (TextView) convertView.findViewById(R.id.ad_filetype);
			vh.repeat = (TextView) convertView.findViewById(R.id.ad_repeat);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.index.setText((position+1)+"");
		AdStrategyPattern oneAd = strategyList.get(position);
		if (oneAd != null) {
			vh.name.setText("" + oneAd.advertiser);
			vh.endDate.setText(res.getString(R.string.ab_item_end_date) + oneAd.endDate);
			vh.adType.setText(res.getString(R.string.ab_item_file_type) + oneAd.minetype);
			vh.repeat.setText(res.getString(R.string.ad_item_repeat) + oneAd.repeat);
            if(null != oneAd.urls && oneAd.urls.size()> 0 ){
    			vh.fileNum.setText(res.getString(R.string.ab_item_file_num) + oneAd.urls.size());
            	for(String url:oneAd.urls){
            		if (!url.equals("")) {
        				MyApplication.imageLoader.displayImage(mFileUtil.convertHttpUrlToLocalFilePath(url) + url, vh.adLog,MyApplication.viewOptions);
        				break;
        			}
            	}
            }else{
    			vh.fileNum.setText(res.getString(R.string.ab_item_file_num)+"0");
            }
		} else {
			vh.name.setText("未定义");
			vh.endDate.setText("00-00-00");
			vh.adType.setText("");
			vh.repeat.setText("0");
			vh.fileNum.setText("0");
		}
		return convertView;
	}

	class ViewHolder {
		TextView index;
		ImageView adLog;
		TextView name;
		TextView agency;
		TextView endDate;
		TextView fileNum;
		TextView adType;
		TextView repeat;		
	}
}
