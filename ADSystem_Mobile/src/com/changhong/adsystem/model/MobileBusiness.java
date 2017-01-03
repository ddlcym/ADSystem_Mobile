package com.changhong.adsystem.model;

/**
 * User: Jack Wang
 * Date: 16-4-1
 * Time: 上午8:55
 */
public class MobileBusiness {

    /************************************手机登录系统*************************************/

    /**
     * 发送验证码
     */
    public final static String MOBILE_SMS_SEND = "mobile_sms_send";

    /**
     * 手机登录系统
     * <p>
     * 1 - 用户先输入短信验证码
     * 2 - 服务端检查是否登录成功
     * 3 - 检查是否有更新
     */
    public final static String MOBILE_LOGIN = "mobile_login";

    /************************************手机查询小区*************************************/

    /**
     * 小区手机端查询
     */
    public final static String MOBILE_COMMUNITY_SEARCH = "mobile_community_search";

    /*
     * 手机号码
     */
    public final static String MOBILE="mobile";
}
