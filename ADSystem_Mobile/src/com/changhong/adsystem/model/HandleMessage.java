package com.changhong.adsystem.model;

/**
 * User: Jack Wang
 * Date: 16-4-1
 * Time: 上午8:16
 */
public enum HandleMessage {

    MESSAGE_OK("处理成立"),
    MESSAGE_FAIL("处理失败"),
    MESSAGE_DATA_NOTFOUND("请求数据参数不全"),
    MESSAGE_CODE_OVERDUE("动态秘钥失效"),

    MESSAGE_MOBILE_NOT_REGISTER("该手机号未登记到系统"),
    MESSAGE_MOBILE_CODE_OVERDUE("验证码不正确或者失效");


    private String description;

    HandleMessage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
