/**
 * 基于JSON 封装格式 ：通用格式
 */

package com.changhong.adsystem.model;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


public class JsonSendModel {

	String LOG_TAG = "YDInfor";
	private static JsonSendModel instance = null;
	private int SerialNumber = 0;
	
	private Map<String, Object> sendParams = null;

	public static JsonSendModel getInstance() {
		if (null == instance) {
			instance = new JsonSendModel();
		}
		return instance;
	}

	public void init(Map<String, Object> param) {

		if (null != sendParams) {
			sendParams.clear();
			sendParams = null;
		}
		this.sendParams = param;
		Log.i(LOG_TAG, "JsonSendModel::   init（）++++++++++++++++++++++++++");				

	}


	private byte[] sendMsgWithHead(byte[] data) {
		return null;
	}

	/**
	 * 封装通信帧格式
	 * 
	 * @param sendParams
	 * @param frameLength
	 * @return
	 */
	private String formatDataToString(Map<String, Object> sendParams) {
		
		return null;
    }
	
	
	
	

	public byte[] getSendMsg(byte[] data) {

		return sendMsgWithHead(data);
	}
	
	
	
	private JSONArray getJSONOjectFromStr(String text) {

		JSONArray attrArray= new JSONArray();
		if (null != text && text.length() > 0) {

			String[] items=text.split(",");
		
			for (int i = 0; i < items.length; i++) {
				JSONObject attrObject = new JSONObject();		
				String[] unit=items[i].split(":");
				if(2==unit.length){
					String key=unit[0];
					String value=unit[1];
					try {
						attrObject.put("attrID", key);
						attrObject.put("attrValue", value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				attrArray.put(attrObject);
			}
		}
		return attrArray;
	}

	public byte[] getJSONData() {
		return null;
	}

}
