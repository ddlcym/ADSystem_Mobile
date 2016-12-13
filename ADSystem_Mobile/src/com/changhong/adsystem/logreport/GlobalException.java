package com.changhong.adsystem.logreport;

import java.lang.Thread.UncaughtExceptionHandler;

/** 
 * @author  cym  
 * @date 创建时间：2016年12月5日 下午3:57:18 
 * @version 1.0 
 * @parameter   
 */

public class GlobalException implements UncaughtExceptionHandler {

	private final static GlobalException myCrashHandler = new GlobalException(); 
	
	private  GlobalException() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		// 在这里捕获异常信息  
		
	}

	public static synchronized GlobalException getInstance(){
		return myCrashHandler;
	}
	

	
}