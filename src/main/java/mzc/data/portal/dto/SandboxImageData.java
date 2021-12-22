package mzc.data.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mzc.data.portal.enums.SandboxImageStatus;
import org.springframework.data.relational.core.mapping.Column;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SandboxImageData {

    private long sandboxImageIdx;
    private String sandboxType;
    private String sandboxOs;
    private String cloudImageId;
    private String sandboxImageDescription;
    private String sandboxImageTags;
    private SandboxImageStatus sandboxImageStatus;
    private long sandboxImageVolume;
    private long sandboxImageCreatedUserIdx;
    private LocalDateTime sandboxImageCreatedAt;
    private long sandboxImageUpdatedUserIdx;
    private LocalDateTime sandboxImageUpdatedAt;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSandboxImageInfoParam {

        private long sandboxImageIdx;
        private String sandboxType;
        private String sandboxOs;
        private String cloudImageId;
        private String sandboxImageDescription;
        private String sandboxImageTags;
        private SandboxImageStatus sandboxImageStatus;
        private long sandboxImageVolume;
        private long sandboxImageCreatedUserIdx;
        private LocalDateTime sandboxImageCreatedAt;
        private long sandboxImageUpdatedUserIdx;
        private LocalDateTime sandboxImageUpdatedAt;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddSandboxImageParam {
        private String sandboxType;
        private String sandboxOs;
        private String cloudImageId;

        //        @NotNull(message = "이미지 설명을 입력해주세요.")
//        @NotBlank(message = "이미지 설명을 입력해주세요.")
        private String sandboxImageDescription;
        private String sandboxImageTags;
        private SandboxImageStatus sandboxImageStatus;
        private long sandboxImageVolume;
        private long sandboxImageCreatedUserIdx;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSandboxImageParam {

        private long sandboxImageIdx;
        private String sandboxImageDescription;
        private SandboxImageStatus sandboxImageStatus;
        private long sandboxImageUpdatedUserIdx;
        private LocalDateTime sandboxImageUpdatedAt;
    }

    /*
    응답 데이터
     */
    @NoArgsConstructor
    public static class SandboxImageDataRes<T> extends ApiResponse<T> {
        public SandboxImageDataRes(int status, String error, T data) {
            super(status, error, data);
        }
    }
}
