package com.quanxiaoha.xiaohashu.agent.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgentRewriteReq {
    @NotBlank(message = "内容不能为空")
    private String content;
}
