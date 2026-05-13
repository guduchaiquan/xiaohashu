package com.quanxiaoha.xiaohashu.note.biz.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentVO {
    private Long id;
    private Long noteId;
    private Long userId;
    private String userNickname;
    private String userAvatar;
    private String content;
    private Date createTime;
}
