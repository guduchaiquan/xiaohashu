package com.quanxiaoha.xiaohashu.note.biz.model.vo;

import lombok.Data;

@Data
public class SearchReqVO {
    private String keyword;
    private Integer pageNo = 1;
    private Integer pageSize = 10;
}
