package com.quanxiaoha.xiaohashu.auth.enums;

import com.quanxiaoha.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum ResponseCodeEnum implements BaseExceptionInterface {

    // 省略...

    // ----------- 业务异常状态码 -----------
    VERIFICATION_CODE_SEND_FREQUENTLY("AUTH-20000", "请求太频繁，请3分钟后再试"),
    PARAM_NOT_VALID("400", "Invalid Parameters"),  // 示例，确保此行存在
    SUCCESS("200", "Success"),
    ERROR("500", "Server Error"),
    VERIFICATION_CODE_ERROR("AUTH-20001","验证码错误"),
    INVALID_LOGIN_TYPE("AUTH-20002", "无效的登录类型，请使用验证码登录(1)或密码登录(2)"),
    SYSTEM_ERROR("500", "System Error"),
    LOGIN_TYPE_ERROR("AUTH-20002", "登录类型错误"),
    USER_NOT_FOUND("AUTH-20003", "该用户不存在"),
    PHONE_OR_PASSWORD_ERROR("AUTH-20004", "手机号或密码错误"),
    LOGIN_FAIL("AUTH-20005", "登录失败"),
    ;


    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

}


