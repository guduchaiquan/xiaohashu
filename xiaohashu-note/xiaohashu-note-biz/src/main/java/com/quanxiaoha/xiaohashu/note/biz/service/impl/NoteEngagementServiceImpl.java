package com.quanxiaoha.xiaohashu.note.biz.service.impl;

import com.quanxiaoha.framework.common.context.LoginUserContextHolder;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.CommentCreateReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.CommentVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteInteractReqVO;
import com.quanxiaoha.xiaohashu.note.biz.service.NoteEngagementService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class NoteEngagementServiceImpl implements NoteEngagementService {

    private final Map<Long, AtomicLong> likeCounter = new ConcurrentHashMap<>();
    private final Map<Long, AtomicLong> favoriteCounter = new ConcurrentHashMap<>();
    private final Map<Long, List<CommentVO>> commentStore = new ConcurrentHashMap<>();
    private final AtomicLong commentIdGen = new AtomicLong(10000);

    @Override
    public Response<?> like(NoteInteractReqVO reqVO) {
        long count = likeCounter.computeIfAbsent(reqVO.getNoteId(), key -> new AtomicLong()).incrementAndGet();
        return Response.success(Map.of("noteId", reqVO.getNoteId(), "action", "like", "result", true, "likeCount", count));
    }

    @Override
    public Response<?> unlike(NoteInteractReqVO reqVO) {
        long count = likeCounter.computeIfAbsent(reqVO.getNoteId(), key -> new AtomicLong()).updateAndGet(v -> Math.max(0, v - 1));
        return Response.success(Map.of("noteId", reqVO.getNoteId(), "action", "unlike", "result", true, "likeCount", count));
    }

    @Override
    public Response<?> favorite(NoteInteractReqVO reqVO) {
        long count = favoriteCounter.computeIfAbsent(reqVO.getNoteId(), key -> new AtomicLong()).incrementAndGet();
        return Response.success(Map.of("noteId", reqVO.getNoteId(), "action", "favorite", "result", true, "favoriteCount", count));
    }

    @Override
    public Response<?> unfavorite(NoteInteractReqVO reqVO) {
        long count = favoriteCounter.computeIfAbsent(reqVO.getNoteId(), key -> new AtomicLong()).updateAndGet(v -> Math.max(0, v - 1));
        return Response.success(Map.of("noteId", reqVO.getNoteId(), "action", "unfavorite", "result", true, "favoriteCount", count));
    }

    @Override
    public Response<?> addComment(CommentCreateReqVO reqVO) {
        CommentVO commentVO = CommentVO.builder()
                .id(commentIdGen.incrementAndGet())
                .noteId(reqVO.getNoteId())
                .userId(LoginUserContextHolder.getUserId())
                .userNickname("小哈同学")
                .userAvatar(null)
                .content(reqVO.getContent())
                .createTime(new Date())
                .build();
        commentStore.computeIfAbsent(reqVO.getNoteId(), key -> new java.util.ArrayList<>()).add(commentVO);
        return Response.success(commentVO);
    }

    @Override
    public Response<?> commentList(NoteInteractReqVO reqVO) {
        return Response.success(commentStore.getOrDefault(reqVO.getNoteId(), List.of()));
    }
}
