package mzc.data.portal.dto;

import lombok.*;
import mzc.data.portal.agent.dto.TagData;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SagemakerData {

    private long idx;
    private String boardTitle;
    private String boardText;
    private String boardPassword;
    private LocalDateTime createdAt;
    private long createdUserId;
    private LocalDateTime updatedAt;
    private long updatedUserId;

    @NoArgsConstructor
    public static class SagemakerDataCustomRes<T> extends ApiResponse<T> {
        public SagemakerDataCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateSagemakerInstanceParam {
        private String instanceName;
        private String instanceType;
        private int EbsVolume;
        private List<TagData> cloudTags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateSagemakerInstanceParam {
        private String instanceName;
        private String instanceType;
        private int EbsVolume;
        private List<TagData> cloudTags;
    }

//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class AddNotebookParam {
//        private String notebookInstanceName;
//        private String instanceType;
//    }

}
