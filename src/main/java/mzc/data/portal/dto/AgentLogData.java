package mzc.data.portal.dto;

import lombok.*;

@Getter
public class AgentLogData {
    private String txId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddAgentLogParam {
        private String txId;
        private String uri;
        private String method;
        private String clientId;
        private String clientIp;
        private String headers;
        private String params;
        private String body;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateAgentLogParam {
        private String txId;
        private long executeTime;
        private int statusCode;
        private String responseBody;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetAgentLogParam {
        private String txId;
    }
}
