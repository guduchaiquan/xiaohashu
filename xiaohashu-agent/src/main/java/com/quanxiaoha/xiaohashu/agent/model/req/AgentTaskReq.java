package com.quanxiaoha.xiaohashu.agent.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgentTaskReq {
    @NotBlank(message = "内容不能为空")
    private String content;

    /**
     * 会话 ID，用于上下文和记忆隔离。
     */
    private String sessionId;

    /**
     * 用户偏好标签，例如："种草风, 干货风"。
     */
    private String preferences;
}
