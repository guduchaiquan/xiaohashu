package com.quanxiaoha.framework.common.constant.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusEnum {
    //启用
    ENABLE(0),
    DISABLE(1);
    private final Integer value;
}
