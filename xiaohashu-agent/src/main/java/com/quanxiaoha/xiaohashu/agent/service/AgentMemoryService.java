package com.quanxiaoha.xiaohashu.agent.service;

import java.util.List;

public interface AgentMemoryService {
    void remember(String sessionId, String content, List<String> titles, String rewrite, List<String> tags, List<String> contextHints);

    String summarize(String sessionId);

    List<String> recentContext(String sessionId);
}
