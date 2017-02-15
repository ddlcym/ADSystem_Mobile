package com.changhong.adsystem.model;


import org.json.JSONException;
import org.json.JSONObject;
import com.changhong.adsystem.utils.ServiceConfig;

public class JsonSendModel {

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

}
