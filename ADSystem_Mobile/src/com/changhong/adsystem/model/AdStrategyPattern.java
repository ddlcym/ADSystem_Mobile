package com.changhong.adsystem.model;

import java.util.List;

public class AdStrategyPattern {
	public String id;//设备ID
	public int defaultDuration;//默认播放持续时间
	public int index;	//播放序列
	public String startDate;	//开始日期
	public String endDate;	//结束日期	
	public String advertiser;	//名称
	public String agency;	    //代理
	public String minetype;//资源类型
	public int repeat;//重复次数
	public List<String> urls;	//资源文件集的路径
	
}
