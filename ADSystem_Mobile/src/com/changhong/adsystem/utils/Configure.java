package com.changhong.adsystem.utils;

import java.io.File;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;


public class Configure {
	
	//广告策略文件转存地址：
	
	public static String rootPath=Environment.getRootDirectory().getAbsolutePath();
	public static String adBaseFilePath=rootPath+File.separator+"adSystem/baseFile";

    public static String serverIP = null;
	//执行结果定义
  	public final static String  ACTION_SUCCESS= "success";
  	public final static String  ACTION_FAILED= "failed";
  	public static final int SHOW_ACTION_RESULT = 0;
	
	/***********************************************通信方式定义********************************************************/
	public final static int ACTION_HTTP_DOWNLOAD= 1001;//数据下载
	public final static int ACTION_HTTP_PUSH= 1002;//信息推送

	public final static int ACTION_SOCKET_COMMUNICATION= 1006;
	public static final int COMMUNICATION_ERROR = 1000;

	public static final int HTTP_MAX_WATING_TIME = 30000;//通讯最大延迟时间

	
	//本地通讯地址：
	public static String socketAddr="192.168.0.16:8080";

	public static final int MSG_SUCCESS = 0;//通讯成功 
	public static final int MSG_FAILURE = 1;//通讯失败       
	//通讯方式定义
	public static final int MSG_SOCKET = 1;//socket通讯
	public static final int MSG_HTTP_POST = 2;//http通讯post方式。
	public static final int MSG_HTTP_GET = 3;//http通讯GET方式。

	/***************************************************文件定义************************************************************/
	public final static String  FILE_TYPE= "fileType";
	public final static String  FILE_EDIT= "fileEdit";
	public final static String  EDIT_TYPE= "editType";
	public final static String  FILE_EXIST= "fileExist";
	public final static String  FILE_LARGE= "largeFile";
	public final static String  FILE_NOTFOUND= "fileNotFound";

	
}
