package com.changhong.adsystem.utils;


public class ServiceConfig {
	

    public static String P2P_SERVER_IP = "0:0:0:0";
    public static int P2P_SERVER_PORT = 20147;

	//执行结果定义
  	public final static String  ACTION_SUCCESS= "success";
  	public final static String  ACTION_FAILED= "failed";
  	public static final int SHOW_ACTION_RESULT = 0;
	
	/***********************************************通信方式定义********************************************************/
	public final static int ACTION_HTTP_DOWNLOAD= 1001;//数据下载
	public final static int ACTION_P2P_TCPSOCKET= 1002;//P2P-TCPIP通讯方式
	public final static int ACTION_P2P_UDP= 1003;//P2P-UDP通讯方式
	public static final int ACTION_P2P_ERROR = 1000;
	public static final int HTTP_MAX_WATING_TIME = 30000;//通讯最大延迟时间

	public static final int MSG_SUCCESS = 0;//通讯成功 
	public static final int MSG_FAILURE = 1;//通讯失败       

}