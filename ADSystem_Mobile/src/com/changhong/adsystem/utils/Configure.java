package com.changhong.adsystem.utils;

import java.io.File;

import android.media.audiofx.EnvironmentalReverb;
import android.os.Environment;


public class Configure {
	
	//广告策略文件转存地址：
	
	public static String rootPath=Environment.getRootDirectory().getAbsolutePath();
	public static String adBaseFilePath=rootPath+File.separator+"adSystem/baseFile";

	/***************************************************文件定义************************************************************/
	public final static String  FILE_TYPE= "fileType";
	public final static String  FILE_EDIT= "fileEdit";
	public final static String  EDIT_TYPE= "editType";
	public final static String  FILE_EXIST= "fileExist";
	public final static String  FILE_LARGE= "largeFile";
	public final static String  FILE_NOTFOUND= "fileNotFound";

	
}
