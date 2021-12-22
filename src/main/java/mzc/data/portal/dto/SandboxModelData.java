package mzc.data.portal.dto;

import lombok.*;
import mzc.data.portal.enums.SandboxImageStatus;
import mzc.data.portal.enums.SandboxModelStatus;
import mzc.data.portal.enums.SandboxSpecStatus;
import mzc.data.portal.enums.SandboxType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Getter
public class SandboxModelData {

    private long sandboxModelIdx;
    private String sandboxModelName;
    private long sandboxSpecIdx;
    private long sandboxImageIdx;
    private String sandboxModelDescription;
    private String sandboxModelTags;
    private SandboxModelStatus sandboxModelStatus;
    private long sandboxModelCreatedUserIdx;
    private LocalDateTime sandboxModelCreatedAt;
    private long sandboxModelUpdatedUserIdx;
    private LocalDateTime sandboxModelUpdatedAt;
    SandboxType sandboxModelType;

    private String sandboxImageType;
    private String sandboxImageOs;
    private String sandboxImageName;
    private String cloudImageId;
    private String sandboxImageDescription;
    private String sandboxImageTags;
    SandboxImageStatus sandboxImageStatus;
    private long sandboxImageVolume;
    private long sandboxImageCreatedUserIdx;
    private LocalDateTime sandboxImageCreatedAt;
    private long sandboxImageUpdatedUserIdx;
    private LocalDateTime sandboxImageUpdatedAt;

    private String sandboxSpecType;
    private String sandboxSpecOs;
    private String sandboxSpecInstanceType;
    private long sandboxSpecCpu;
    private long sandboxSpecMemory;
    private String sandboxSpecNetworkDescription;
    private double sandboxSpecPrice;
    SandboxSpecStatus sandboxSpecStatus;
    private LocalDateTime sandboxSpecCreatedAt;
    private LocalDateTime sandboxSpecUpdatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddSandboxModelParam {

        @NotNull(message = "모델 이름을 입력해주세요.")
        @NotBlank(message = "모델 이름을 입력해주세요.")
        private String sandboxModelName;

        private long sandboxSpecIdx;
        private long sandboxImageIdx;

        @NotNull(message = "모델 설명을 입력해주세요.")
        @NotBlank(message = "모델 설명을 입력해주세요.")
        private String sandboxModelDescription;

        private String sandboxModelTags;
        private SandboxModelStatus sandboxModelStatus;
        private long sandboxModelCreatedUserIdx;
        private LocalDateTime sandboxModelCreatedAt;

        SandboxType sandboxModelType;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSandboxModelInfoParam {

        private long sandboxModelIdx;
        private String sandboxModelName;
        private long sandboxSpecIdx;
        private long sandboxImageIdx;
        private String sandboxModelDescription;
        private String sandboxModelTags;
        private SandboxModelStatus sandboxModelStatus;
        private long sandboxModelCreatedUserIdx;
        private LocalDateTime sandboxModelCreatedAt;
        private long sandboxModelUpdatedUserIdx;
        private LocalDateTime sandboxModelUpdatedAt;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSandboxModelParam {

        private long sandboxModelIdx;
        private String sandboxModelDescription;
        private SandboxModelStatus sandboxModelStatus;
        private long sandboxModelUpdatedUserIdx;
        private LocalDateTime sandboxModelUpdatedAt;
    }

    /*
    응답 데이터
     */
    @NoArgsConstructor
    public static class SandboxModelDataRes<T> extends ApiResponse<T> {
        public SandboxModelDataRes(int status, String error, T data) {
            super(status, error, data);
        }
    }

}
