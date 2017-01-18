package com.changhong.adsystem.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import com.changhong.adsystem.model.CommunityInfor;
import com.changhong.adsystem_mobile.R;



public class CommunityAdapter extends BaseAdapter {

    private LayoutInflater minflater;

    private List<CommunityInfor> strategyList;

    public CommunityAdapter(Context context, List<CommunityInfor> strategyList) {
        this.minflater = LayoutInflater.from(context);
        this.strategyList = strategyList;
    }
    /**
     * 更新数据，更新list后在调用notifyDataSetChanged()
     * @param ipList
     */
    public void updateList(List<CommunityInfor> strategyList) {
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
            convertView = minflater.inflate(R.layout.ad_community_item, null); 
            vh.Name = (TextView)convertView.findViewById(R.id.item_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        
        if(strategyList.get(position)!=null){
        	vh.Name.setText(""+strategyList.get(position).comName);
        }else{
        	vh.Name.setText("未定义");
        }        
        return convertView;
    }

   class ViewHolder {
        public TextView Name;
    }
}
