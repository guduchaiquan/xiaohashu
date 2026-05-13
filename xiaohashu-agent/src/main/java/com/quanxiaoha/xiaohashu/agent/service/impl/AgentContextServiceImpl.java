package com.quanxiaoha.xiaohashu.agent.service.impl;

import com.quanxiaoha.xiaohashu.agent.service.AgentContextService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgentContextServiceImpl implements AgentContextService {

    @Override
    public List<String> buildContextHints(String content, String preferences) {
        List<String> hints = new ArrayList<>();
        if (content == null || content.isBlank()) {
            hints.add("内容为空，建议先补充正文再生成标题和标签");
            return hints;
        }
        if (content.length() < 20) {
            hints.add("内容较短，建议补充场景、结果或个人体验");
        }
        if (content.contains("!")) {
            hints.add("语气较强，可以稍微柔和一点更像种草风");
        }
        if (preferences != null && !preferences.isBlank()) {
            hints.add("用户偏好：" + preferences);
        }
        hints.add("优先突出利益点、场景和体验感");
        return hints;
    }
}
