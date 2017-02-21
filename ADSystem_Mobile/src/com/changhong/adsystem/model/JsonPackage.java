package com.changhong.adsystem.model;


import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.adsystem.nanohttpd.HTTPDService;
import com.changhong.adsystem.p2p.DataUtil;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;

public class JsonPackage {
	
	 static int SerialNumber=1;
	/**
	 * socketServer 返回信息
	 * @param action 命令
	 * @return
	 */
	public static String socketRespMsg(String action,boolean isSuccess) {
		JSONObject obj=new JSONObject();
		int resID=R.string.tcpserver_response_failed;

		try {
			if(ServiceConfig.TCPS_SERVER_FILEDOWNLOAD.equals(action)){
				obj.put(ServiceConfig.TCP_SOCKET_ACTION, action);
				if(isSuccess && !action.isEmpty())resID=R.string.tcpserver_response_success;
				obj.put(ServiceConfig.TCP_SOCKET_RESPOND, resID);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return obj.toString();
	}

	
	
	
	

	/**
	 * 封装通信帧格式
	 * 
	 * @param sendParams
	 * @param frameLength
	 * @return
	 */
	public static String formateTcpSendMsg(String action, String msg) {
		JSONObject json = null;
		try {
			json = new JSONObject();
			json.put(ServiceConfig.TCP_SOCKET_ACTION, action);	
			if(msg.isEmpty()){
				json.put(ServiceConfig.TCP_SOCKET_REQUEST, new JSONObject());
			}else{
				json.put(ServiceConfig.TCP_SOCKET_REQUEST, new JSONObject(msg));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (null == json) ? "" : json.toString();
	}
	
	
	/**
	 * 封装Str为下载参数信息
	 * @return
	 */
	public static String getDownLoadConf() {
		JSONObject json = null;
		try {
			json = new JSONObject();
			String confUrl="http://"+P2PService.getIp()+":"+HTTPDService.HTTP_PORT+"/"+Configure.adBaseFileSuffix;
			String reUrl="http://"+P2PService.getIp()+":"+HTTPDService.HTTP_PORT+"/"+Configure.adResPrefix;
			json.put("configUrl", confUrl);
			json.put("resourceUrlPrefix",reUrl);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return (null == json) ? "" : json.toString();
	}
	
	public static String queryDeviceInfoParams(){
		JSONObject obj=new JSONObject();
		try {
			JSONObject json = new JSONObject();
			obj.put("action","getSTBInfo");
			obj.put("request", json);			
		} catch (JSONException e) {
			e.printStackTrace();
		}			
	  return obj.toString();
	}
	
	
	
	public static byte[] sendMsgToByte(String sendMsg) {

		
		if(null == sendMsg)return null;
		
		DataUtil dataUtil = new DataUtil();
		byte[] formateData = null;
		byte[] data=sendMsg.getBytes();
		int serialNumber = getSerialNumber();
		int length=data.length;
		// 数据封装
		byte[] b0 = dataUtil.int32ToByte(data.length);//字符长度
		byte[] b1 = dataUtil.int32ToByte(serialNumber); // 序号 自增		
		byte[] b2 = dataUtil.int32ToByte("mobile_requst".hashCode()); //参数
	
		formateData = dataUtil.addBytes(b0, b1);
		formateData = dataUtil.addBytes(formateData, b2);		
		//JSOn定义的数据格式，支持多包传送
		formateData = dataUtil.addBytes(formateData, data);
		
		int lengt=dataUtil.byteToInt32(b0, 0);
		return formateData;
	}
	
	private static int getSerialNumber() {

		SerialNumber++;
		if (65535 <= SerialNumber) {
			SerialNumber = 0;
		}
		return SerialNumber;
	}

}
