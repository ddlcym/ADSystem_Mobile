package com.changhong.adsystem.logreport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** 
 * @author  cym  
 * @date 创建时间：2016年12月7日 上午10:09:49 
 * @version 1.0 
 * @parameter   
 */
public class MyDate {

	public static String getDateEN(){
		
		return "";
	}
	
	
	
	public static String getFileName(){
		SimpleDateFormat formatter; 
	    formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss",Locale.CHINA); 
	    String ctime = formatter.format(new Date()); 

	    return ctime; 
	}
}
