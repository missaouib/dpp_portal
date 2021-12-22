package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mzc.data.portal.enums.SandboxOS;
import software.amazon.awssdk.services.ec2.model.InstanceBlockDeviceMapping;
import software.amazon.awssdk.services.ec2.model.InstanceStateName;
import software.amazon.awssdk.services.ec2.model.Tag;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class InstanceData {

    String instanceId;
    String publicDns;
    String publicIp;
    String privateIp;
    List<InstanceBlockDeviceMapping> ebsId;
    InstanceStateName status;
    private String creationDate;
    private Instant startedAt;
    private List<Tag> tags;

    public boolean isCompleted() {
        if (this.ebsId == null) {
            return false;
        }

        if (this.status != InstanceStateName.RUNNING) {
            return false;
        }

        return true;
    }

    public boolean isPending() {
        if (this.status != InstanceStateName.PENDING) {
            return false;
        }

        return true;
    }

    public boolean isRunning() {
        if (this.status != InstanceStateName.RUNNING) {
            return false;
        }

        return true;
    }

    public boolean isStopping() {
        if (this.status != InstanceStateName.STOPPING) {
            return false;
        }

        return true;
    }

    public boolean isStopped() {
        if (this.status != InstanceStateName.STOPPED) {
            return false;
        }

        return true;
    }

    public boolean isTerminated() {
        if (this.status != InstanceStateName.TERMINATED) {
            return false;
        }

        return true;
    }

    public static InstanceData of(String instanceId, List<Tag> tags) {
        return InstanceData.builder().instanceId(instanceId).status(InstanceStateName.PENDING).tags(tags).build();
    }

    @Builder
    private InstanceData(String instanceId, String publicDns, String publicIp, String privateIp, List<InstanceBlockDeviceMapping> ebsId, InstanceStateName status, String creationDate, List<Tag> tags) {
        this.instanceId = instanceId;
        this.publicDns = publicDns;
        this.publicIp = publicIp;
        this.privateIp = privateIp;
        this.ebsId = ebsId;
        this.status = status;
        this.creationDate = creationDate;
        this.tags = tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateInstanceParam {
        private String amiId;
        private String instanceType;
        private SandboxOS instanceOs;
        private int EbsVolume;
        private List<TagData> cloudTags;
        private InstanceConfigData instanceConfigData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteInstanceParam {
        private String instanceId;
    }


}
