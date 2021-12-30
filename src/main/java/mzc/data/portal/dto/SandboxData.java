package mzc.data.portal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import mzc.data.portal.enums.CloudSandboxInstanceStatus;
import mzc.data.portal.enums.SandboxStatus;
import mzc.data.portal.enums.SandboxType;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Getter
public class SandboxData {

    //Mapper.xml을 통해 조회된 샌드박스 데이터, 조회용이기에 기본적으로 Getter만 사용
    private long idx;
    private String name;
    private long sandboxModelIdx;
    private int volume;
    private String cloudInstanceUuid;
    private SandboxStatus status;
    private String description;
    private LocalDateTime createdAt;
    private long createdUserIdx;
    private LocalDateTime updatedAt;
    private long updatedUserIdx;
    private SandboxType type;
    private String autoStopYN;
//    private String instaceId;
//    private String connectionPrivateUrl;
//    private String connectionPublicUrl;
//    private String cloudStatus;
    private LocalDateTime startedAt;
    private LocalDateTime stoppedAt;
    private LocalDateTime cloudCreatedAt;
    private LocalDateTime cloudUpdatedAt;

    // cloud_sandbox_instances table
    private long cloudIdx;
    private String cloudInstanceId;
    private CloudSandboxInstanceStatus cloudStatus;
    private String cloudConnectionPrivateUrl;
    private String cloudConnectionPublicUrl;
    SandboxType cloudInstanceType;

    SandboxType sandboxModelType;

    //HTML Form을 통해 DB에 사용될 파라미터
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetProjectSandboxParam {
        @NotNull(message = "sandboxId must be not null")
        private long sandboxIdx;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddSandboxParam {

        @NotNull(message = "샌드박스 이름을 작성해주세요")
        @NotBlank(message = "샌드박스 이름을 작성해주세요.")
        @Size(min = 1, max = 20, message = "샌드박스 이름은 최소 {min} , 최대 {max} 자리까지 입력 가능합니다.")
        private String name;

//        @NotNull(message = "ec2 샌드박스 모델을 선택해주세요")
//        @Min(value = 1, message = "샌드박스 모델을 선택해주세요")
        private String sandboxModelIdx;

//        @NotNull(message = "ec2 샌드박스 모델을 선택해주세요")
//        @Min(value = 1, message = "샌드박스 모델을 선택해주세요")
//        @DefaultValue(0)
        private long Ec2SandboxModelIdx;

//        @NotNull(message = "sagemaker 샌드박스 모델을 선택해주세요")
//        @Min(value = 1, message = "샌드박스 모델을 선택해주세요")
        private long sagemakerSandboxModelIdx;

        @NotNull(message = "샌드박스 용량을 입력해주세요.")
        @Min(value = 200, message = "샌드박스 용량은 256GB 이상 입력주세요")
        @Max(value = 1024, message = "샌드박스 용량은 1TB를 초과하실 수 없습니다")
        private int volume;

        private String cloudInstanceUuid = "388a99ed-9486-4a46-aeb6-06eaf6c47675";

        @JsonIgnore
        private SandboxStatus status;

        @NotNull(message = "샌드박스 설명을 작성해주세요")
        @NotBlank(message = "샌드박스 설명을 작성해주세요.")
        @Size(min = 1, max = 1024, message = "샌드박스 설명은 최소 {min} , 최대 {max} 자리까지 입력 가능합니다.")
        private String description;

        @JsonIgnore
        private long userIdx;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSandboxParam {
        private long idx;
        private long userIdx;
        private String instanceUuid;
        private SandboxStatus sandboxStatus;

    }

    //TODO Models인데 ModelData가 있는지 없는지 확인하고, 옮길지 말지 결정하기.
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Models {
        private long sandboxModelIdx;
        private String sandboxModelName;
        private String sandboxModelDescription;
//        private SandboxModelDivision sandboxModelDivision;
        private int sandboxImageVolume;
        private String sandboxInstanceType;
        private String sandboxType;
        private String sandboxOs;
        private int sandboxCpu;
        private int sandboxMemory;
        private double sandboxPrice;
    }


    //TODO data type sync
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddNotebookParam {
        private String notebookInstanceName;
        private String instanceType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TerminateSandboxParam {

        private long idx;

        @JsonIgnore
        private SandboxStatus status;

        @JsonIgnore
        private long userIdx;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ModelParam {
        private String type;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AutoStopSandboxParam {
        private List<String> idx;
//        private String name;
//        private String cloudInstanceUuid;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SandboxSagemakerParam {
        private long idx;
    }



        /*
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class findSandboxDetailCloudSandboxInstanceParam {
        private String joinType;
    }

    /*
    Custom 응답 데이터
     */
    @NoArgsConstructor
    public static class SandboxDataCustomRes<T> extends ApiResponse<T> {
        public SandboxDataCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }

}
