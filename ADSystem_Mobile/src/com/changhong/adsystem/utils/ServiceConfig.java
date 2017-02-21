package com.changhong.adsystem.utils;


public class ServiceConfig {
	

    public static String P2P_SERVER_IP = "192.168.1.100";
    public static  int P2P_SERVER_PORT = 20147;
	public static String MAC="12:12:5D:13:B5:1F";
  	public static final int SOCKET_MAX_WATING_TIME = 60;

	
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
	
	/************************************************文件下载定义************************************************************/
	public final static int IMAGE_DOWNLOAD_START= 2000;//文件下载成功
	public final static int IMAGE_EXIST= 2001;//文件已经存在
	public final static int IMAGE_DOWNLOAD_FINISHED= 2002;//文件下载完成
	public final static int IMAGE_DOWNLOAD_FAILED= 2003;//文件下载失败
	
	/********************************************与STB通讯的常量定义************************************************************************/
  	public final static String  TCP_SOCKET_ACTION= "action";
  	public final static String  TCP_SOCKET_REQUEST= "request";
  	public final static String  TCP_SOCKET_RESPOND= "response";
  	public final static String  TCP_SOCKET_BEATS= "_hb";

  	
  	public final static String  TCPS_ACTION_STBINFOR= "getSTBInfo";
  	public final static String  TCPS_ACTION_DOWNLOADCONF= "pushDownloadConf";
  	public final static String  TCPS_SERVER_FILEDOWNLOAD= "fileDownloadPro";
  	public final static String  TCPS_SERVER_FILEDOWNLOAD_FINISHED= "fileDownloadPro";

  	
  	public final static int  TCPS_ACTION_STBINFOR_CODE= 3001;
  	public final static int  TCPS_ACTION_DOWNLOADCONF_CODE= 3002;
  	
  	
  	/***************************************************TCP 通讯类型定义****************************************************************/
  	
	public final static int  TCP_SOCKET_TYPE_REQUESR= 4001;
  	public final static int  TCP_SOCKET_TYPE_SEND_BEATS= 4002;
  	public final static int  TCP_SOCKET_TYPE_RECEIVE_BEATS= 4003;
  	public final static int  TCP_SOCKET_TYPE_CREATECONNECT= 4004;
  	public final static int  TCP_SOCKET_TYPE_RESPOND= 4005;

  
}
