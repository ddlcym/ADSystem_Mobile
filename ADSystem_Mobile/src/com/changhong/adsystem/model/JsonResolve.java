package com.changhong.adsystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.changhong.adsystem.utils.AdStrategyPattern;

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
	
	public String id;//设备ID
	public int defaultDuration;//默认播放持续时间
	public int uuid;//资源id	
	public int index;	//播放序列
	public String advertiser;	//名称
	public String agency;	    //代理
	public int  duration;//持续时长
	public String minetype;//资源类型
	public int repeat;//重复次数
	public String url;	//资源请求路径
	
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
				adStrat.uuid=getJsonObjectString(itemObj,"uuid");
				adStrat.index=getJsonObjInt(itemObj,"index");
				adStrat.advertiser=getJsonObjectString(itemObj,"advertiser");
				adStrat.agency=getJsonObjectString(itemObj,"agency");
				adStrat.duration=getJsonObjInt(itemObj,"duration");
				adStrat.repeat=getJsonObjInt(itemObj,"repeat");
				adStrat.url=getJsonObjectString(itemObj,"url");
				strategyPatterns.add(adStrat);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}			
		return strategyPatterns; 
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
