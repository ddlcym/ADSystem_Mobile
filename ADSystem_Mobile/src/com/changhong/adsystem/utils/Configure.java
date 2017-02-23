package com.changhong.adsystem.utils;

import java.io.File;
import android.os.Environment;


public class Configure {
	
	//广告策略文件转存地址：
	
	public static String rootPath=Environment.getExternalStorageDirectory().getAbsolutePath();

	public static String adBaseFileSuffix="adSystem/baseFile/spJson.json";
	public static String adResPrefix="adSystem/res/";
	public static String adBaseFilePath=rootPath+File.separator+adBaseFileSuffix;
	public static String adResFilePath=rootPath+File.separator+adResPrefix;
	/***************************************************文件定义************************************************************/
	public final static String  FILE_TYPE= "fileType";
	public final static String  FILE_EDIT= "fileEdit";
	public final static String  EDIT_TYPE= "editType";
	public final static String  FILE_EXIST= "fileExist";
	public final static String  FILE_LARGE= "largeFile";
	public final static String  FILE_NOTFOUND= "fileNotFound";

	
	//UDP通信方式定义
	
	public final static int UDPQueryResult=3001;
	public final static int UDPQueryTimeOut=3002;	
	public final static int UDPQuerySuccess=3003;
	public final static int UDPQueryFailed=3004;	
	public final static int WifiConnect=3005;	
	public final static int WifiClosed=3006;	

	//TCP通信定义
	public final static int TCP_DEVICES_INFO=3101;
	
}
