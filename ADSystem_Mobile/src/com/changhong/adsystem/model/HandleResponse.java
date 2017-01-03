package com.changhong.adsystem.model;

import com.alibaba.fastjson.JSONObject;

/**
 * User: Jack Wang
 * Date: 16-6-27
 * Time: 下午1:26
 */
public class HandleResponse {

    public final static String APP_TYPE = "appType";
    public final static String APP_VERSION = "appVersion";
    public final static String BUSINESS_TYPE = "businessType";
    public final static String REQUEST_BODY = "body";

    public final static String REQUEST_MAC = "mac";

    public final static String MESSAGE_TILE = "message";
    public final static String MESSAGE_STATUS = "status";
    public final static String RESPONSE_BODY = "body";

    /*********************************************分发不同的REQUEST***************************************************/

    public static JSONObject generateDataNotFoundResponse() {
        JSONObject jsonObject = new JSONObject();
        generateDataNotFoundResponse(jsonObject);
        return jsonObject;
    }

    public static JSONObject generateFailedResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_FAIL.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.FAILED);
        return jsonObject;
    }

    public static JSONObject generateDataNotFoundResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_DATA_NOTFOUND.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.DATA_NOT_RIGHT);
        return jsonObject;
    }

    public static JSONObject generateOKResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_OK.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.OK);
        return jsonObject;
    }

    public static JSONObject generateFailedResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_FAIL.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.FAILED);
        return jsonObject;
    }

    public static JSONObject generateCodeOverdueResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_CODE_OVERDUE.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.BOX_CODE_OVERDUE);
        return jsonObject;
    }

    /*********************************************MOBILE部分***************************************************/

    public static JSONObject generateMoblileNotFoundResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_MOBILE_NOT_REGISTER.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.MOBILE_NOT_REGISTER);
        return jsonObject;
    }

    public static JSONObject generateMobileCodeOverdueResponse(JSONObject jsonObject) {
        jsonObject.put(MESSAGE_TILE, HandleMessage.MESSAGE_MOBILE_CODE_OVERDUE.getDescription());
        jsonObject.put(MESSAGE_STATUS, HandleStatus.MOBILE_CODE_OVERDUE);
        return jsonObject;
    }
}
