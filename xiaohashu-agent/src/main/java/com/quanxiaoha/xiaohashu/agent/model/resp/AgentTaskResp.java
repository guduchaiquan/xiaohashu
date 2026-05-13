package com.quanxiaoha.xiaohashu.agent.model.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AgentTaskResp {
    private String sessionId;
    private List<String> titles;
    private String rewrite;
    private List<String> tags;
    private List<String> contextHints;
    private String memorySummary;
}
