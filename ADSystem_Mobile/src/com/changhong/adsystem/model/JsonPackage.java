package com.changhong.adsystem.model;


import org.json.JSONException;
import org.json.JSONObject;

import com.changhong.adsystem.nanohttpd.HTTPDService;
import com.changhong.adsystem.p2p.P2PService;
import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.ServiceConfig;

public class JsonPackage {

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

}
