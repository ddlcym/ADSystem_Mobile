package com.changhong.adsystem.socket;


public class SocketConfig {
	
	
	public final static int PORT = 8830;
	public final static  String USERNAME = "abc";//川网
//	public final static String HOST = "220.166.62.57";//绵阳;
//	public final static  String USERNAME = "wlgs";//绵阳
//	public final static String HOST = "120.31.135.2";//"192.168.112.82";
//	public final static String HOST = "192.168.12.20";//广东文博会	
	public final static String HOST = "113.107.234.91";//广东研究院
	public final static String PASSWORD = "123456";
//	public final static String doMain = "stb.bj";
//	public final static String doMain = "wlgs.my";//绵阳
	public final static String doMain = "abc.wl";//川网

	public  static String mUserName ="lhy1";//绵阳;
//	public  static String mUserName ="abc";//川网

	public  static String mPassWord = "123456";

	
    /***************************************消息类型*********************************************/

//    /*基于JSON信息命令类型*/		
	public final static String MESSAGE_ACTION_UPDATE ="getVersion";//获取系统版本号;
	public final static String MESSAGE_ACTION_DEVICE_LIST ="getDeviceList";//获取家电列表请求;
	public final static String MESSAGE_ACTION_POSITION_LIST ="getDevicePositionList";//获取家电房间列表请求;		



}
