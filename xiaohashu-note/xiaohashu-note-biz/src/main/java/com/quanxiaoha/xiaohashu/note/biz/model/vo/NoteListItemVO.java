package com.quanxiaoha.xiaohashu.note.biz.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class NoteListItemVO {
    private Long id;
    private String title;
    private String content;
    private List<String> imgUris;
    private String videoUri;
    private Long creatorId;
    private String creatorNickname;
    private String creatorAvatar;
    private Long topicId;
    private String topicName;
    private Date createTime;
    private Long likeCount;
    private Long favoriteCount;
    private Long commentCount;
}
