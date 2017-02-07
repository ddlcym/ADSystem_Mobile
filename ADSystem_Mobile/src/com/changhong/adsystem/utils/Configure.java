package com.changhong.adsystem.utils;

import java.io.File;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;


public class Configure {
	
	//广告策略文件转存地址：
	
	public static String rootPath=Environment.getDataDirectory().getAbsolutePath();
	public static String adFileUrl=rootPath+File.separator+"adSystem";

	
	public static final int HTTP_MAX_WATING_TIME = 30000;//通讯最大延迟时间

	//本地通讯地址：
	public static String socketAddr="192.168.0.16:8080";

	public static final int MSG_SUCCESS = 0;//通讯成功 
	public static final int MSG_FAILURE = 1;//通讯失败       
	//通讯方式定义
	public static final int MSG_SOCKET = 1;//socket通讯
	public static final int MSG_HTTP_POST = 2;//http通讯post方式。
	public static final int MSG_HTTP_GET = 3;//http通讯GET方式。

	
	
	
}
