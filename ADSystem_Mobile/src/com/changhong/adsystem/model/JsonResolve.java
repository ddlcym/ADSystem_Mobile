package com.changhong.adsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.changhong.adsystem.utils.Configure;
import com.changhong.adsystem.utils.FileUtil;


import android.util.Log;


/** 
 * @author  cym  
 * @date 创建时间：2016年12月21日 下午5:17:29 
 * @version 1.0 
 * @parameter   
 */
public class JsonResolve {
	static FileUtil mFileUtil=new FileUtil();
	
	public static String resolveSecurityCode(JSONObject json){
		String result=getJsonObjectString(json,"message");		
		return result;
	}
	
	
	
	/**
	 * 解析小区信息列表
	 * @param jsonStr
	 * @return
	 */
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
				comm.devList=getDevices(itemObj);
				comm.devNum=(null == comm.devList)?0:comm.devList.size();
				communitylist.add(comm);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
		return communitylist; 
	}
	
	
	
	public static List<DeviceInfor> getDevices(JSONObject json){
		List DeviceInfor= new ArrayList<DeviceInfor>();
		if(null == json)return DeviceInfor;
		
		try {
			JSONArray array=getJsonObjectArray(json,"descs");
			int size=(null == array)?0:array.length();
			for (int i = 0; i <size; i++) {
				JSONObject itemObj=array.getJSONObject(i);
				DeviceInfor dev=new DeviceInfor();
				dev.id=getJsonObjectString(itemObj,"uuid");
				dev.mac=getJsonObjectString(itemObj,"mac");
				dev.ssid=getJsonObjectString(itemObj,"ssid");
				DeviceInfor.add(dev);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
		return DeviceInfor; 
	}
	

	/**
	 * 获取广告策略
	 * @param jsonStr
	 * @return
	 */
	public static List<AdStrategyPattern> getStrategyPatterns(String jsonStr){
		List strategyPatterns= new ArrayList<AdStrategyPattern>();
		if(null == jsonStr)return strategyPatterns;
		try {
			JSONObject json = new JSONObject(jsonStr);
			String devID=getJsonObjectString(json,"ID");
			int defaultDuration=getJsonObjInt(json,"defaultDuration");
			JSONArray array=getJsonObjectArray(json,"playList");			
			int size=(null == array)?0:array.length();
			for (int i = 0; i <size; i++) {
				JSONObject itemObj=array.getJSONObject(i);
				AdStrategyPattern adStrat=new AdStrategyPattern();
				adStrat.id=devID;
				adStrat.defaultDuration=defaultDuration;
				adStrat.index=getJsonObjInt(itemObj,"index");
				adStrat.advertiser=getJsonObjectString(itemObj,"advertiser");
				adStrat.repeat=getJsonObjInt(itemObj,"repeat");
				adStrat.startDate=getJsonObjectString(itemObj,"startDate");
				adStrat.endDate=getJsonObjectString(itemObj,"endDate");
				JSONArray files=getJsonObjectArray(itemObj,"files");
				int fileNum=(null == files)?0:files.length();
				adStrat.urls=new ArrayList<String>();
                for (int j = 0; j < fileNum; j++) {
    				JSONObject fileObj=files.getJSONObject(j);
    				String url=getJsonObjectString(fileObj,"url");
    				String minetype=getJsonObjectString(fileObj,"mineType");
                    if(null == adStrat.minetype){
                    	adStrat.minetype=minetype;
    				}
    				if(null != url && url.length()>2){
    					adStrat.urls.add(url);
    					break;
    				}   				
				}				
				strategyPatterns.add(adStrat);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
		return strategyPatterns; 
	}
	
	public static String formateAdSPJson(){
		JSONObject spObj=new JSONObject();
		try {
			String spjson=mFileUtil.readFileFromSDCard(Configure.adBaseFilePath);
			spjson=spjson.replace(" ", "");
			JSONObject json = new JSONObject(spjson);
			spObj.put("urlPrefix", Configure.adResPrefix);
			spObj.put("body", json);			
			Log.i("AdSPJson", "formate Json  OK! ");	
		} catch (JSONException e) {
			e.printStackTrace();
		}			
	  return spObj.toString();
	}
	
	
	/**
	 * 转换资源存储地址为本地url地址
	 * @param jsonStr
	 */
	public static String converHttpUrl2LocaUrl(String jsonStr){
		if(null == jsonStr)return "";
		String locationJson=jsonStr;
	    String prefix="adSystem";
		try {
			JSONObject json = new JSONObject(jsonStr);
			JSONArray array=getJsonObjectArray(json,"playList");			
			int size=(null == array)?0:array.length();
			for (int i = 0; i <size; i++) {
				JSONObject itemObj=array.getJSONObject(i);
				if(null == itemObj)continue;
				JSONArray files=getJsonObjectArray(json,"files");
				int fileNum=(null == files)?0:files.length();
                for (int j = 0; j < fileNum; j++) {
    				JSONObject fileObj=files.getJSONObject(j);
    				if(null == fileObj)continue;
    				String url=getJsonObjectString(fileObj,"url");
    				if(null != url && url.length() >2){
    				    locationJson=locationJson.replace(url, Configure.adResPrefix+url);
    				}
				}				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
		return locationJson; 
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
	
	public static JSONObject getJsonObj(String str) {
		JSONObject jsonObj = null;
		try {
			jsonObj=new JSONObject(str);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
