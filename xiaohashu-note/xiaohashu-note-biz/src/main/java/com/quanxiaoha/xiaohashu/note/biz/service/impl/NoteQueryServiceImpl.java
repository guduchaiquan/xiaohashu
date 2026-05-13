package com.quanxiaoha.xiaohashu.note.biz.service.impl;

import com.quanxiaoha.framework.common.context.LoginUserContextHolder;
import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.note.biz.domain.dataobject.NoteDO;
import com.quanxiaoha.xiaohashu.note.biz.domain.mapper.NoteDOMapper;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteDetailReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteDetailVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NoteListItemVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.NotePageReqVO;
import com.quanxiaoha.xiaohashu.note.biz.model.vo.SearchReqVO;
import com.quanxiaoha.xiaohashu.note.biz.rpc.KeyValueRpcService;
import com.quanxiaoha.xiaohashu.note.biz.rpc.UserRpcService;
import com.quanxiaoha.xiaohashu.note.biz.service.NoteQueryService;
import com.quanxiaoha.xiaohashu.user.dto.req.FindUserByIdReqDTO;
import com.quanxiaoha.xiaohashu.user.dto.resp.FindUserByIdRspDTO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NoteQueryServiceImpl implements NoteQueryService {

    @Resource
    private NoteDOMapper noteDOMapper;
    @Resource
    private KeyValueRpcService keyValueRpcService;
    @Resource
    private UserRpcService userRpcService;

    @Override
    public Response<?> detail(NoteDetailReqVO reqVO) {
        NoteDO noteDO = noteDOMapper.selectByPrimaryKey(reqVO.getId());
        if (Objects.isNull(noteDO)) {
            return Response.success(null);
        }
        FindUserByIdRspDTO user = userRpcService.findById(FindUserByIdReqDTO.builder().id(noteDO.getCreatorId()).build()).getData();
        String content = StringUtils.isNotBlank(noteDO.getContentUuid()) ? keyValueRpcService.getNoteContent(noteDO.getContentUuid()) : null;
        return Response.success(NoteDetailVO.builder()
                .id(noteDO.getId())
                .title(noteDO.getTitle())
                .content(content)
                .imgUris(StringUtils.isBlank(noteDO.getImgUris()) ? Collections.emptyList() : Arrays.asList(noteDO.getImgUris().split(",")))
                .videoUri(noteDO.getVideoUri())
                .creatorId(noteDO.getCreatorId())
                .creatorNickname(user == null ? "未知用户" : user.getNickName())
                .creatorAvatar(user == null ? null : user.getAvatar())
                .creatorIntroduction("分享生活灵感")
                .topicId(noteDO.getTopicId())
                .topicName(noteDO.getTopicName())
                .createTime(noteDO.getCreateTime())
                .likeCount(0L)
                .favoriteCount(0L)
                .commentCount(0L)
                .liked(Boolean.FALSE)
                .favorited(Boolean.FALSE)
                .build());
    }

    @Override
    public Response<?> page(NotePageReqVO reqVO) {
        return Response.success(buildList(reqVO));
    }

    @Override
    public Response<?> myPage(NotePageReqVO reqVO) {
        reqVO.setCreatorId(LoginUserContextHolder.getUserId());
        return Response.success(buildList(reqVO));
    }

    @Override
    public Response<?> topicPage(NotePageReqVO reqVO) {
        return Response.success(buildList(reqVO));
    }

    @Override
    public Response<?> searchUsers(SearchReqVO reqVO) {
        return Response.success(List.of(FindUserByIdRspDTO.builder().id(1L).nickName(reqVO.getKeyword() + "用户").avatar(null).build()));
    }

    @Override
    public Response<?> searchNotes(SearchReqVO reqVO) {
        NotePageReqVO pageReqVO = new NotePageReqVO();
        pageReqVO.setKeyword(reqVO.getKeyword());
        return Response.success(buildList(pageReqVO));
    }

    @Override
    public Response<?> searchTags(SearchReqVO reqVO) {
        if (StringUtils.isBlank(reqVO.getKeyword())) {
            return Response.success(Collections.emptyList());
        }
        return Response.success(List.of(reqVO.getKeyword(), reqVO.getKeyword() + "分享", reqVO.getKeyword() + "攻略"));
    }

    private List<NoteListItemVO> buildList(NotePageReqVO reqVO) {
        List<NoteDO> noteDOS = noteDOMapper.selectAll();
        return noteDOS.stream()
                .filter(noteDO -> reqVO.getCreatorId() == null || Objects.equals(noteDO.getCreatorId(), reqVO.getCreatorId()))
                .filter(noteDO -> reqVO.getTopicId() == null || Objects.equals(noteDO.getTopicId(), reqVO.getTopicId()))
                .filter(noteDO -> StringUtils.isBlank(reqVO.getKeyword()) || StringUtils.containsIgnoreCase(noteDO.getTitle(), reqVO.getKeyword()))
                .map(noteDO -> {
                    FindUserByIdRspDTO user = userRpcService.findById(FindUserByIdReqDTO.builder().id(noteDO.getCreatorId()).build()).getData();
                    return NoteListItemVO.builder()
                            .id(noteDO.getId())
                            .title(noteDO.getTitle())
                            .content(StringUtils.isNotBlank(noteDO.getContentUuid()) ? keyValueRpcService.getNoteContent(noteDO.getContentUuid()) : null)
                            .imgUris(StringUtils.isBlank(noteDO.getImgUris()) ? Collections.emptyList() : Arrays.asList(noteDO.getImgUris().split(",")))
                            .videoUri(noteDO.getVideoUri())
                            .creatorId(noteDO.getCreatorId())
                            .creatorNickname(user == null ? "未知用户" : user.getNickName())
                            .creatorAvatar(user == null ? null : user.getAvatar())
                            .topicId(noteDO.getTopicId())
                            .topicName(noteDO.getTopicName())
                            .createTime(noteDO.getCreateTime())
                            .likeCount(0L)
                            .favoriteCount(0L)
                            .commentCount(0L)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
