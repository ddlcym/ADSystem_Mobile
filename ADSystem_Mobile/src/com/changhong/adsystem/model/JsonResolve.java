package com.changhong.adsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	
	
	
	
	
	
	
	public static List<CommunityInfor> getComunnitys(String jsonStr){
		List communitylist= new ArrayList<CommunityInfor>();
		if(null == jsonStr)return communitylist;
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray array=getJsonObjectArray(json,"communities");
			int size=(null == array)?0:array.length();
			for (int i = 0; i <size; i++) {
				JSONObject itemObj=array.getJSONObject(i);
				CommunityInfor comm=new CommunityInfor();
				comm.comID=getJsonObjectString(itemObj,"id");
				comm.comName=getJsonObjectString(itemObj,"name");
				comm.comLocation=getJsonObjectString(itemObj,"location");
				communitylist.add(comm);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
			
		return communitylist; 
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
