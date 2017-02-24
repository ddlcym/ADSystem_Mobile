package com.changhong.adsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.changhong.adsystem.model.DeviceInfor;
import com.changhong.common.service.ClientSendCommandService;

import java.util.List;


public class DeviceSelectAdapter extends BaseAdapter {

    private LayoutInflater minflater;

    private List<DeviceInfor> devList;

    public DeviceSelectAdapter(Context context, List<DeviceInfor> devList) {
        this.minflater = LayoutInflater.from(context);
        this.devList = devList;
    }

    @Override
    public int getCount() {
        return devList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * VIEW HOLDER的配置
         */
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = minflater.inflate(android.R.layout.simple_list_item_1, null);
            vh.boxInfo = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        String tyyInfor = devList.get(position).ssid+";"+devList.get(position).psw;

        vh.boxInfo.setText("投影仪"+ " [" + tyyInfor + "]");

        return convertView;
    }

    public final class ViewHolder {
        public TextView boxInfo;
    }
}
