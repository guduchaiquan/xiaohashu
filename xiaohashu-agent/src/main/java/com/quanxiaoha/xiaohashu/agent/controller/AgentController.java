package com.quanxiaoha.xiaohashu.agent.controller;

import com.quanxiaoha.framework.common.response.Response;
import com.quanxiaoha.xiaohashu.agent.model.req.AgentRewriteReq;
import com.quanxiaoha.xiaohashu.agent.model.req.AgentTagReq;
import com.quanxiaoha.xiaohashu.agent.model.req.AgentTitleReq;
import com.quanxiaoha.xiaohashu.agent.service.AgentService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Resource
    private AgentService agentService;

    @PostMapping("/title")
    public Response<?> generateTitles(@Valid @RequestBody AgentTitleReq req) {
        return Response.success(agentService.generateTitles(req.getContent()));
    }

    @PostMapping("/rewrite")
    public Response<?> rewriteContent(@Valid @RequestBody AgentRewriteReq req) {
        return Response.success(agentService.rewriteContent(req.getContent()));
    }

    @PostMapping("/tags")
    public Response<?> recommendTags(@Valid @RequestBody AgentTagReq req) {
        return Response.success(agentService.recommendTags(req.getContent()));
    }
}
