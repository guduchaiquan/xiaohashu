package com.quanxiaoha.xiaohashu.note.biz.rpc;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "xiaohashu-kv")
public interface KeyValueRpcService {

    @PostMapping("/kv/note/content/save")
    boolean saveNoteContent(@RequestParam("contentUuid") String contentUuid, @RequestParam("content") String content);

    @PostMapping("/kv/note/content/get")
    String getNoteContent(@RequestParam("contentUuid") String contentUuid);

    @PostMapping("/kv/note/content/delete")
    boolean deleteNoteContent(@RequestParam("contentUuid") String contentUuid);
}
