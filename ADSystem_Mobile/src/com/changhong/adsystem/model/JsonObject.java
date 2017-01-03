package com.changhong.adsystem.model;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonObject {

	String TAG = "YDInfor";

	static JsonObject mJsonObject = null;

	// 保存解析后的数据信息
	private Map<String, Object> readParams = null;
	public static final int FRAME_HEAD_LENGTH = 12;
	private static final int BUFFER_MAX_SIZE = 1500 - FRAME_HEAD_LENGTH;
	boolean[] reFlag = { false, false, false, false, false };

	private JsonObject() {
		readParams = new HashMap<String, Object>();
	}

	public static JsonObject getJsonObject() {
		if (null == mJsonObject) {
			mJsonObject = new JsonObject();
		}
		return mJsonObject;
	}

	/**
	 * 解析本地数据
	 * 
	 * @param rContent
	 * @return
	 */
	public Map<String, Object> ParserLocalMsg(String action ,byte[] Buffer) {
		
		return null;
	}
	
	/**
	 * 业务平台--HTTP服务器返回数据。 解析透传数据
	 * 
	 * @param rContent
	 * @return
	 */
	public Map<String, Object> ParserHttpMsg(String msgAction ,String rContent) {
		return null;
    }
	
	



	
/*****************************************************json 公用解析方法***********************************************/

	private int getJsonObjectValueInt(JSONObject jsonObj, String key) {

		int rValue = 0;
		try {
			rValue = jsonObj.getInt(key);		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rValue;
	}
	
	
	private float getJsonObjectValueFloat(JSONObject jsonObj, String key) {

		float rValue = 0;
		try {
			String floatStr = jsonObj.getString(key);
			if(null != floatStr){
				rValue=Float.parseFloat(floatStr);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rValue;
	}

	private String getJsonObjectValue(JSONObject jsonObj, String key) {

		String rValue = null;
		try {
			rValue = jsonObj.getString(key).trim();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rValue;
	}

	private boolean getJsonObjectValueBool(JSONObject jsonObj, String key) {

		boolean rValue = false;
		try {
			rValue = jsonObj.getBoolean(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rValue;

	}

	private JSONArray getJsonObjectArray(JSONObject jsonObj, String key) {

		JSONArray rValue = null;
		
		if(null == jsonObj ||  null == key )return null;

		try {
			rValue = jsonObj.getJSONArray(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rValue;

	}

	
	private JSONObject getJsonObject(JSONObject jsonObj, String key) {

		JSONObject rValue = null;
		
		if(null == jsonObj ||  null == key )return null;
		
		try {			
			rValue = jsonObj.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rValue;
	}

	public boolean ParserMsgFromStr(String result) {

		return false;
	}
	
	
	

}
