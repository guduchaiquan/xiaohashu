package com.quanxiaoha.xiaohashu.agent.service;

import java.util.List;

public interface AgentService {
    List<String> generateTitles(String content);

    String rewriteContent(String content);

    List<String> recommendTags(String content);
}
