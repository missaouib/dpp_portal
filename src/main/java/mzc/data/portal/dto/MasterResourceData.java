package mzc.data.portal.dto;

import lombok.*;
import mzc.data.portal.enums.MasterResourceGroup;
import mzc.data.portal.enums.MasterResourceStatus;

@Getter
public class MasterResourceData {
    private String type;
    private String name;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetMasterResourceParam {
        private MasterResourceGroup group;
        private MasterResourceStatus status;
    }
}
