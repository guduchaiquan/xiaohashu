package com.quanxiaoha.xiaohashu.kv.biz.controller;

import com.quanxiaoha.framework.common.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/kv/note/content")
public class KVController {

    private static final Map<String, String> CONTENT_STORE = new ConcurrentHashMap<>();

    @PostMapping("/save")
    public Response<Boolean> saveNoteContent(@RequestParam("contentUuid") String contentUuid,
                                             @RequestParam("content") String content) {
        CONTENT_STORE.put(contentUuid, content);
        return Response.success(true);
    }

    @PostMapping("/get")
    public Response<String> getNoteContent(@RequestParam("contentUuid") String contentUuid) {
        return Response.success(CONTENT_STORE.get(contentUuid));
    }

    @PostMapping("/delete")
    public Response<Boolean> deleteNoteContent(@RequestParam("contentUuid") String contentUuid) {
        CONTENT_STORE.remove(contentUuid);
        return Response.success(true);
    }
}
