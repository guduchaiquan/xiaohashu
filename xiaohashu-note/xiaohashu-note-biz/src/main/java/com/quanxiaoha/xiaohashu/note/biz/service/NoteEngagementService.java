package com.quanxiaoha.xiaohashu.note.biz.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.CommentCreateReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteInteractReqVO;

public interface NoteEngagementService {
    Response<?> like(NoteInteractReqVO reqVO);

    Response<?> unlike(NoteInteractReqVO reqVO);

    Response<?> favorite(NoteInteractReqVO reqVO);

    Response<?> unfavorite(NoteInteractReqVO reqVO);

    Response<?> addComment(CommentCreateReqVO reqVO);

    Response<?> commentList(NoteInteractReqVO reqVO);
}
