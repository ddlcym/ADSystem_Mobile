package com.changhong.adsystem.http;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.changhong.adsystem.model.HandleResponse;
import com.changhong.adsystem.model.MobileBusiness;
import com.changhong.adsystem.utils.AesUtils;

/**
 * @author cym
 * @date 创建时间：2016年12月20日 下午2:01:43
 * @version 1.0
 * @parameter
 */
public class RequestURL {
	private static String serverIP = "http://192.168.1.115:8080/";
//	private static String serverIP = "http://192.168.1.101:8080/";
//	// 验证码请求地址
	private static String requestParamsURL = serverIP
			+ "adplatform/mobile/resource.html?json=";
	//test    	
//	private static String serverIP = "http://192.168.1.101:8082/";
//	private static String requestParamsURL = serverIP
//			+ "adplatform/backend/index.html?json=";

	public static String getSecurityCode(String mobile) {
		JSONObject request = new JSONObject();
		request.put(HandleResponse.APP_TYPE, "ANDROID");
		request.put(HandleResponse.APP_VERSION, "1.0");
		request.put(HandleResponse.BUSINESS_TYPE,
				MobileBusiness.MOBILE_SMS_SEND);
		// request .put(HandleResponse., "");

		JSONObject body = new JSONObject();
		body.put(MobileBusiness.MOBILE, mobile);
		request.put(HandleResponse.REQUEST_BODY,
				AesUtils.fixEncrypt(body.toJSONString()));

		return requestParamsURL + request.toJSONString();
	}

	public static String getLoginURL(String mobile, String securityCode) {
		JSONObject request = new JSONObject();
		request.put(HandleResponse.APP_TYPE, "ANDROID");
		request.put(HandleResponse.APP_VERSION, "1.0");
		request.put(HandleResponse.BUSINESS_TYPE, MobileBusiness.MOBILE_LOGIN);

		JSONObject body = new JSONObject();
		body.put("mobile", mobile);
		// body.put("mobile","18982238566");
		body.put("code", securityCode);
		request.put(HandleResponse.REQUEST_BODY,
				AesUtils.fixEncrypt(body.toJSONString()));
		return requestParamsURL + request.toJSONString();
	}

	/*
	 * 根据关键字获取小区列表
	 * 
	 * @param 
	 * words 
	 * 		the keywords of the residential
	 * 
	 * number 
	 * 		the number of residential 
	 */
	public static String getCommunity(String words, int number) {
		if(null == words||number<0){
			return "";
		}
		JSONObject request = new JSONObject();
		request.put(HandleResponse.APP_TYPE, "ANDROID");
		request.put(HandleResponse.APP_VERSION, "1.0");
		request.put(HandleResponse.BUSINESS_TYPE, MobileBusiness.MOBILE_COMMUNITY_SEARCH);

		JSONObject body = new JSONObject();
		body.put("words", words);
		body.put("number", number);
		request.put(HandleResponse.REQUEST_BODY,
				AesUtils.fixEncrypt(body.toJSONString()));

		return requestParamsURL + request.toJSONString();
	}

	
	public static String getStrategyPattern( String mac) {
		if(TextUtils.isEmpty(mac)){
			return "";
		}
		JSONObject request = new JSONObject();
		request.put(HandleResponse.APP_TYPE, "ANDROID");
		request.put(HandleResponse.APP_VERSION, "1.0");
		request.put(HandleResponse.BUSINESS_TYPE, MobileBusiness.MOBILE_COMMUNITY_SEARCH);

		JSONObject body = new JSONObject();
		body.put("mac", mac);
		request.put(HandleResponse.REQUEST_BODY,
				AesUtils.fixEncrypt(body.toJSONString()));

		return requestParamsURL + request.toJSONString();
	}

}
