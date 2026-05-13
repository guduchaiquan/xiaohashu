package com.quanxiaoha.xiaohashu.agent.service.impl;

import com.quanxiaoha.xiaohashu.agent.service.AgentMemoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AgentMemoryServiceImpl implements AgentMemoryService {

    private static final int MAX_HISTORY = 5;
    private final Map<String, Deque<String>> historyMap = new ConcurrentHashMap<>();

    @Override
    public void remember(String sessionId, String content, List<String> titles, String rewrite, List<String> tags, List<String> contextHints) {
        Deque<String> history = historyMap.computeIfAbsent(sessionId, key -> new ArrayDeque<>());
        String snapshot = "content=" + shorten(content) +
                " | titles=" + titles +
                " | tags=" + tags +
                " | hints=" + contextHints;
        synchronized (history) {
            history.addFirst(snapshot);
            while (history.size() > MAX_HISTORY) {
                history.removeLast();
            }
        }
    }

    @Override
    public String summarize(String sessionId) {
        Deque<String> history = historyMap.get(sessionId);
        if (history == null || history.isEmpty()) {
            return "暂无记忆";
        }
        return String.join(" || ", history);
    }

    @Override
    public List<String> recentContext(String sessionId) {
        Deque<String> history = historyMap.get(sessionId);
        if (history == null || history.isEmpty()) {
            return List.of("无历史上下文");
        }
        return new ArrayList<>(history);
    }

    private String shorten(String text) {
        if (text == null) {
            return "";
        }
        return text.length() > 80 ? text.substring(0, 80) + "..." : text;
    }
}
