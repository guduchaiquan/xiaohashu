package com.quanxiaoha.xiaohashu.note.biz.controller;

import com.quanxiaoha.framework.biz.operationlog.aspect.ApiOperationLog;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.CommentCreateReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteDetailReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteInteractReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NotePageReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.PublishNoteReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.SearchReqVO;
import com.quanxiaoha.xiaohashu.note.biz.service.NoteEngagementService;
import com.quanxiaoha.xiaohashu.note.biz.service.NoteQueryService;
import com.quanxiaoha.xiaohashu.note.biz.service.NoteService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/note")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;
    @Resource
    private NoteQueryService noteQueryService;
    @Resource
    private NoteEngagementService noteEngagementService;

    @PostMapping(value = "/publish")
    @ApiOperationLog(description = "笔记发布")
    public Response<?> publishNote(@Validated @RequestBody PublishNoteReqVO publishNoteReqVO) {
        return noteService.publishNote(publishNoteReqVO);
    }

    @PostMapping("/detail")
    @ApiOperationLog(description = "笔记详情")
    public Response<?> detail(@Validated @RequestBody NoteDetailReqVO reqVO) {
        return noteQueryService.detail(reqVO);
    }

    @PostMapping("/page")
    @ApiOperationLog(description = "笔记推荐列表")
    public Response<?> page(@RequestBody NotePageReqVO reqVO) {
        return noteQueryService.page(reqVO);
    }

    @PostMapping("/my/page")
    @ApiOperationLog(description = "我的笔记列表")
    public Response<?> myPage(@RequestBody NotePageReqVO reqVO) {
        return noteQueryService.myPage(reqVO);
    }

    @PostMapping("/topic/page")
    @ApiOperationLog(description = "话题笔记列表")
    public Response<?> topicPage(@RequestBody NotePageReqVO reqVO) {
        return noteQueryService.topicPage(reqVO);
    }

    @PostMapping("/like")
    public Response<?> like(@Validated @RequestBody NoteInteractReqVO reqVO) {
        return noteEngagementService.like(reqVO);
    }

    @PostMapping("/unlike")
    public Response<?> unlike(@Validated @RequestBody NoteInteractReqVO reqVO) {
        return noteEngagementService.unlike(reqVO);
    }

    @PostMapping("/favorite")
    public Response<?> favorite(@Validated @RequestBody NoteInteractReqVO reqVO) {
        return noteEngagementService.favorite(reqVO);
    }

    @PostMapping("/unfavorite")
    public Response<?> unfavorite(@Validated @RequestBody NoteInteractReqVO reqVO) {
        return noteEngagementService.unfavorite(reqVO);
    }

    @PostMapping("/comment/add")
    public Response<?> addComment(@Validated @RequestBody CommentCreateReqVO reqVO) {
        return noteEngagementService.addComment(reqVO);
    }

    @PostMapping("/comment/list")
    public Response<?> commentList(@Validated @RequestBody NoteInteractReqVO reqVO) {
        return noteEngagementService.commentList(reqVO);
    }

    @PostMapping("/search/user")
    public Response<?> searchUser(@RequestBody SearchReqVO reqVO) {
        return noteQueryService.searchUsers(reqVO);
    }

    @PostMapping("/search/note")
    public Response<?> searchNote(@RequestBody SearchReqVO reqVO) {
        return noteQueryService.searchNotes(reqVO);
    }

    @PostMapping("/search/tag")
    public Response<?> searchTag(@RequestBody SearchReqVO reqVO) {
        return noteQueryService.searchTags(reqVO);
    }
}
