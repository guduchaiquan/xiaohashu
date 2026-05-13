package com.quanxiaoha.xiaohashu.note.biz.service;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteDetailReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NotePageReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.SearchReqVO;

public interface NoteQueryService {
    Response<?> detail(NoteDetailReqVO reqVO);

    Response<?> page(NotePageReqVO reqVO);

    Response<?> myPage(NotePageReqVO reqVO);

    Response<?> topicPage(NotePageReqVO reqVO);

    Response<?> searchUsers(SearchReqVO reqVO);

    Response<?> searchNotes(SearchReqVO reqVO);

    Response<?> searchTags(SearchReqVO reqVO);
}
