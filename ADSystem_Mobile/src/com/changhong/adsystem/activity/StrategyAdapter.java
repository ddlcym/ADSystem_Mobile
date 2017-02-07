package com.changhong.adsystem.activity;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import com.changhong.adsystem.utils.AdDetail;
import com.changhong.adsystem_mobile.R;
import com.changhong.common.system.MyApplication;

public class StrategyAdapter extends BaseAdapter {

	private LayoutInflater minflater;

	private List<AdDetail> strategyList;
	private Resources res;

	public StrategyAdapter(Context context, List<AdDetail> strategyList) {
		this.minflater = LayoutInflater.from(context);
		this.strategyList = strategyList;
		res=context.getResources();

	}

	/**
	 * 更新数据，更新list后在调用notifyDataSetChanged()
	 * 
	 * @param ipList
	 */
	public void updateList(List<AdDetail> strategyList) {
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
			vh.adImage = (ImageView) convertView.findViewById(R.id.ad_log);
			vh.name = (TextView) convertView.findViewById(R.id.ad_name);
//			vh.startDate = (TextView) convertView.findViewById(R.id.ad_startdate);
			vh.endDate = (TextView) convertView.findViewById(R.id.ad_enddate);
			vh.type = (TextView) convertView.findViewById(R.id.ad_filetype);
			vh.repeat = (TextView) convertView.findViewById(R.id.ad_repeat);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.index.setText((position+1)+"");
		AdDetail oneAd = strategyList.get(position);
		if (oneAd != null) {
			vh.name.setText("" + oneAd.name);
//			vh.startDate.setText(res.getString(R.string.ab_item_start_date) + oneAd.startDate);
			vh.endDate.setText(res.getString(R.string.ab_item_end_date) + oneAd.startDate);
			vh.type.setText(res.getString(R.string.ab_item_file_type) + oneAd.adType);
			vh.repeat.setText(res.getString(R.string.ad_item_repeat) + oneAd.repeat);

			if (!oneAd.adImagePath.equals("")) {
				MyApplication.imageLoader.displayImage("file://"
						+ oneAd.adImagePath, vh.adImage,
						MyApplication.viewOptions);
			}

		} else {
			vh.name.setText("未定义");
			vh.startDate.setText("00-00-00");
			vh.endDate.setText("00-00-00");
			vh.type.setText("");
			vh.repeat.setText("0");
		}

		return convertView;
	}

	class ViewHolder {
		TextView index;
		ImageView adImage;
		TextView name;
		TextView startDate;
		TextView endDate;
		TextView type;
		TextView repeat;
	}
}
