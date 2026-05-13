package com.quanxiaoha.xiaohashu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NoteInteractReqVO {
    @NotNull(message = "笔记 ID 不能为空")
    private Long noteId;
}
