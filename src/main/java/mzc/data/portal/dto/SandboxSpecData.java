package mzc.data.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mzc.data.portal.enums.SandboxSpecStatus;

import java.time.LocalDateTime;

@Data
public class SandboxSpecData {

    private long sandboxSpecIdx;
    private String sandboxType;
    private String sandboxOs;
    private String instanceType;
    private long sandboxCpu;
    private long sandboxMemory;
    private String sandboxNetworkDescription;
    private float sandboxPrice;
    private SandboxSpecStatus sandboxSpecStatus;
    private LocalDateTime sandboxSpecCreatedAt;
    private LocalDateTime sandboxSpecUpdatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetSandboxSpecInfoParam {

        private long sandboxSpecIdx;
        private String sandboxType;
        private String sandboxOs;
        private String instanceType;
        private long sandboxCpu;
        private long sandboxMemory;
        private String sandboxNetworkDescription;
        private float sandboxPrice;
        private SandboxSpecStatus sandboxSpecStatus;
        private LocalDateTime sandboxSpecCreatedAt;
        private LocalDateTime sandboxSpecUpdatedAt;

    }

}
