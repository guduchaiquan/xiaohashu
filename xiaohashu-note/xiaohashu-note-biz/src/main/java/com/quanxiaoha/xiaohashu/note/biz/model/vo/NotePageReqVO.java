package com.quanxiaoha.xiaohashu.note.biz.model.vo;

import lombok.Data;

@Data
public class NotePageReqVO {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    private Long topicId;
    private Long creatorId;
    private String keyword;
}
