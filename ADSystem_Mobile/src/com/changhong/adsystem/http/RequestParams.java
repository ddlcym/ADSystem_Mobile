package com.changhong.adsystem.http;

import com.alibaba.fastjson.JSONObject;
import com.changhong.adsystem.modle.HandleResponse;
import com.changhong.adsystem.modle.MobileBusiness;
import com.changhong.adsystem.utils.AesUtils;

/** 
 * @author  cym  
 * @date 创建时间：2016年12月20日 下午2:02:02 
 * @version 1.0 
 * @parameter   
 */
public class RequestParams {

	
	public static JSONObject getSecurityCode(String mobile){
		JSONObject request=new JSONObject();
		request .put(HandleResponse.APP_TYPE, "ANDROID");
		request .put(HandleResponse.APP_VERSION, "1.0");
		request .put(HandleResponse.BUSINESS_TYPE, MobileBusiness.MOBILE_SMS_SEND);
//		request .put(HandleResponse., "");
		
		
		JSONObject body=new JSONObject();
		body.put(MobileBusiness.MOBILE, mobile);
		request.put(HandleResponse.REQUEST_BODY, AesUtils.fixEncrypt(body.toJSONString()));
		
		return request;
	}
}
