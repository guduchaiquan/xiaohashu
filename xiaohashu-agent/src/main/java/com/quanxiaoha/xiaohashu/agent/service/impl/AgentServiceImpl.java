package com.quanxiaoha.xiaohashu.agent.service.impl;

import com.quanxiaoha.xiaohashu.agent.service.AgentContextService;
import com.quanxiaoha.xiaohashu.agent.service.AgentMemoryService;
import com.quanxiaoha.xiaohashu.agent.service.AgentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class AgentServiceImpl implements AgentService {

    @Resource
    private AgentContextService agentContextService;
    @Resource
    private AgentMemoryService agentMemoryService;

    @Override
    public List<String> generateTitles(String content) {
        List<String> titles = new ArrayList<>();
        if (content == null || content.isBlank()) {
            return List.of("今天想分享一点生活灵感", "最近很喜欢的一些小发现", "一篇适合收藏的内容笔记");
        }
        String base = content.length() > 16 ? content.substring(0, 16) : content;
        titles.add("我最近发现了：" + base);
        titles.add("这篇内容真的很值得看");
        titles.add("分享一个超实用的小技巧");
        titles.add("记录一下今天的小灵感");
        titles.add("适合收藏的干货总结");
        return titles;
    }

    @Override
    public String rewriteContent(String content) {
        if (content == null || content.isBlank()) {
            return "今天想记录一点生活灵感，后续会继续补充内容。";
        }
        return "【小红书风格润色版】\n" + content.trim() + "\n\n总结一下：这次分享希望对你有帮助～";
    }

    @Override
    public List<String> recommendTags(String content) {
        Set<String> tags = new LinkedHashSet<>();
        if (content != null) {
            String lower = content.toLowerCase();
            if (lower.contains("穿搭") || lower.contains("衣服")) {
                tags.add("穿搭");
                tags.add("每日穿搭");
            }
            if (lower.contains("美食") || lower.contains("咖啡") || lower.contains("探店")) {
                tags.add("美食");
                tags.add("探店");
            }
            if (lower.contains("笔记") || lower.contains("分享") || lower.contains("教程")) {
                tags.add("干货分享");
                tags.add("经验总结");
            }
        }
        tags.add("小红书");
        tags.add("生活记录");
        tags.add("创作灵感");
        return new ArrayList<>(tags);
    }

    public AgentResult runAgent(String content, String sessionId, String preferences) {
        String realSessionId = (sessionId == null || sessionId.isBlank()) ? UUID.randomUUID().toString() : sessionId;
        List<String> hints = agentContextService.buildContextHints(content, preferences);
        List<String> titles = generateTitles(content);
        String rewrite = rewriteContent(content);
        List<String> tags = recommendTags(content);
        String stage = buildStageLabel(content, preferences, hints);
        agentMemoryService.remember(realSessionId, content, titles, rewrite, tags, hints);
        return new AgentResult(realSessionId, stage, titles, rewrite, tags, hints, agentMemoryService.summarize(realSessionId));
    }

    private String buildStageLabel(String content, String preferences, List<String> hints) {
        if (content == null || content.isBlank()) {
            return "INTAKE_BLOCKED";
        }
        if (preferences != null && !preferences.isBlank()) {
            return "MEMORY_WRITE";
        }
        if (!hints.isEmpty()) {
            return "CONTEXTED_REWRITE";
        }
        return "GROUNDING_ONLY";
    }

    public record AgentResult(String sessionId, String stage, List<String> titles, String rewrite, List<String> tags, List<String> contextHints, String memorySummary) {}
}
