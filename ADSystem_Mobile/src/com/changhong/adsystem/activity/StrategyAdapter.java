package com.changhong.adsystem.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import com.changhong.adsystem_mobile.R;



public class StrategyAdapter extends BaseAdapter {

    private LayoutInflater minflater;

    private List<String> strategyList;

    public StrategyAdapter(Context context, List<String> strategyList) {
        this.minflater = LayoutInflater.from(context);
        this.strategyList = strategyList;
        
    }
    /**
     * 更新数据，更新list后在调用notifyDataSetChanged()
     * @param ipList
     */
    public void updateList(List<String> strategyList) {
    	if(null != strategyList){
			this.strategyList=strategyList;
			notifyDataSetChanged();
    	}
	}

    @Override
    public int getCount() {
        return strategyList==null?0:strategyList.size();
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
            vh.log = (ImageView)convertView.findViewById(R.id.ad_log);
            vh.name = (TextView)convertView.findViewById(R.id.ad_name);
            vh.startDate = (TextView)convertView.findViewById(R.id.ad_startdate);
            vh.endDate = (TextView)convertView.findViewById(R.id.ad_enddate);
            vh.type = (TextView)convertView.findViewById(R.id.ad_name);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        
        if(strategyList.get(position)!=null){
        	vh.name.setText(""+strategyList.get(position));
        }else{
        	vh.name.setText("未定义");
        }        
        return convertView;
    }

   class ViewHolder {
	    ImageView log;
        TextView name;
        TextView startDate;
        TextView endDate;
        TextView type;
        TextView repeat;
    }
}
