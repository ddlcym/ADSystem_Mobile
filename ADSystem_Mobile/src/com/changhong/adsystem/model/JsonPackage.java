package com.changhong.adsystem.model;


import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.adsystem.nanohttpd.HTTPDService;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.ServiceConfig;
import com.changhong.adsystem_mobile.R;

public class JsonPackage {
	
	
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
			json.put(ServiceConfig.TCP_SOCKET_REQUEST, msg);
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
			String confUrl=P2PService.getLocalIpAddress()+":"+ServiceConfig.P2P_SERVER_PORT+"/"+Configure.adBaseFileSuffix;
			String reUrl=P2PService.getLocalIpAddress()+":"+HTTPDService.HTTP_PORT+"/"+Configure.adResPrefix;
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

}
