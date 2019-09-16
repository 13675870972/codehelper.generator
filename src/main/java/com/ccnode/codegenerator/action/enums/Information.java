package com.ccnode.codegenerator.action.enums;

/**
 * @Author: caoyc
 * @Date: 2019-09-15 15:52
 * @Description:
 */
public enum Information {

    SUCCESS("恭喜你生成数据库表成功.", "成功"),
    TIME_ERROR("无法使用.", "失败"),
    PACKAGE_ERROR("禁止使用.", "失败"),
    CONFIG_ERROR("配置有误.", "失败"),
    ;

    private String code;
    private String message;

    Information(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
