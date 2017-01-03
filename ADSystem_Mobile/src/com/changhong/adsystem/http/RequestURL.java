package com.changhong.adsystem.http;

import com.alibaba.fastjson.JSONObject;
import com.changhong.adsystem.model.HandleResponse;
import com.changhong.adsystem.model.MobileBusiness;
import com.changhong.adsystem.utils.AesUtils;

/** 
 * @author  cym  
 * @date 创建时间：2016年12月20日 下午2:01:43 
 * @version 1.0 
 * @parameter   
 */
public class RequestURL {
	private static String serverIP="http://192.168.1.101:8080/";
	
	//验证码请求地址
	private static String getSecurityCode  =serverIP+"adplatform/mobile/resource.html";
	
	
	
	public static String getSecurityCode(String mobile){
		JSONObject request=new JSONObject();
		request .put(HandleResponse.APP_TYPE, "ANDROID");
		request .put(HandleResponse.APP_VERSION, "1.0");
		request .put(HandleResponse.BUSINESS_TYPE, MobileBusiness.MOBILE_SMS_SEND);
//		request .put(HandleResponse., "");
		
		
		JSONObject body=new JSONObject();
		body.put(MobileBusiness.MOBILE, mobile);
		request.put(HandleResponse.REQUEST_BODY, AesUtils.fixEncrypt(body.toJSONString()));
		
		return getSecurityCode+"?"+request.toJSONString();
	}
	
}
