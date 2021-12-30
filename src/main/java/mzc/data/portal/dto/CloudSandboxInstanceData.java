package mzc.data.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mzc.data.portal.agent.dto.TagData;
import mzc.data.portal.enums.CloudSandboxInstanceStatus;
import mzc.data.portal.enums.SandboxType;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
public class CloudSandboxInstanceData {
    private long idx;
    private String instanceUuid;
    private String instanceId;
    private String instanceType;
    private String amiId;
    private String connectionPrivateUrl;
    private String connectionPublicUrl;
    private String Status;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private LocalDateTime terminatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddCloudSandboxInstanceParam {
        @NotNull(message = "sandboxIdx must be not null")
        private long idx;

        @JsonIgnore
        private String instanceUuid;
        @JsonIgnore
        private String instanceId;
        @JsonIgnore
        private String amiId;
        @JsonIgnore
        private String instanceType;
        @JsonIgnore
        private int instanceVolume;
        @JsonIgnore
        private CloudSandboxInstanceStatus cloudSandboxInstanceStatus;
        @JsonIgnore
        private List<TagData> cloudTags;
        @JsonIgnore
        private String cloudTagsJson;
        @JsonIgnore
        private long cloudSandboxInstanceId;
        @JsonIgnore
        private String updatedMemberId;
        @JsonIgnore
        SandboxType sandboxType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateStatusCloudSandboxInstanceParam {
        @NotNull(message = "idx is not null")
        private long idx;
        private String instanceUuid;
        private CloudSandboxInstanceStatus cloudSandboxInstanceStatus;
        private String connectionPrivateUrl;
        private String connectionPublicUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateCloudSandboxInstanceParam {
        private long idx;
        private CloudSandboxInstanceStatus cloudSandboxInstanceStatus;
        private String connectionPrivateUrl;
        private String connectionPublicUrl;
        private boolean isRunning;
        private boolean isStopped;
        private boolean isTerminated;
        private LocalDateTime startedAt;
    }


    /**
     * Custom 응답 데이터
     */
    @NoArgsConstructor
    public static class CloudSandboxInstanceDataCustomRes<T> extends ApiResponse<T> {
        public CloudSandboxInstanceDataCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }



}
