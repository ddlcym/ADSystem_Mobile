package com.changhong.adsystem.utils;
/** 
 * @author  cym  
 * @date 创建时间：2017年2月15日 下午3:30:46 
 * @version 1.0 
 * @parameter   
 */
public class StringUtils {

	
	public static long strToLong(String str){
		long l=0;
		l=Long.parseLong(str);
		return l;
	}
}
