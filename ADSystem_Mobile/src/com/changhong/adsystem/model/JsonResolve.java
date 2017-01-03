package com.changhong.adsystem.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


/** 
 * @author  cym  
 * @date 创建时间：2016年12月21日 下午5:17:29 
 * @version 1.0 
 * @parameter   
 */
public class JsonResolve {

	
	public static String resolveSecurityCode(JSONObject json){
		String result=getJsonObjectString(json,"message");
		
		
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	//======================================捕获json解析异常=========================
	
	public static String getJsonObjectString(JSONObject jsonObj, String key) {

		String rValue = "";
		try {
			rValue = jsonObj.getString(key);
		} catch (JSONException ex) {
			ex.printStackTrace();
			Log.e("mmmm", "JsonResolve:" + key);
		}
		return rValue;
	}

	public static JSONArray getJsonObjectArray(JSONObject jsonObj, String key) {

		JSONArray rValue = null;
		try {
			rValue = jsonObj.getJSONArray(key);
		} catch (JSONException ex) {
			ex.printStackTrace();
			Log.e("mmmm", "JsonResolve:" + key);
		}
		return rValue;
	}

	public static int getJsonObjInt(JSONObject json, String key) {
		int i = 9999;
		try {
			i = json.getInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("mmmm", "JsonResolve:" + key);
		}
		return i;
	}
}
