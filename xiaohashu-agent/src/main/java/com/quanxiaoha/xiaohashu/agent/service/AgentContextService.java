package com.quanxiaoha.xiaohashu.agent.service;

import java.util.List;

public interface AgentContextService {
    List<String> buildContextHints(String content, String preferences);
}
