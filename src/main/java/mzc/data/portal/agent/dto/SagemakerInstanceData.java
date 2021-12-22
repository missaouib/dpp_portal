package mzc.data.portal.agent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mzc.data.portal.enums.SandboxOS;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.sagemaker.model.NotebookInstanceStatus;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class SagemakerInstanceData {

    String instanceId;
    String url;
    String arn;
    NotebookInstanceStatus status;
    private Instant creationTime;
    private Instant lastModifiedTime;
    private List<Tag> tags;

    public boolean isCompleted() {

        if (this.status != NotebookInstanceStatus.IN_SERVICE) {
            return false;
        }

        return true;
    }

    public boolean isPending() {
        if (this.status != NotebookInstanceStatus.PENDING) {
            return false;
        }

        return true;
    }

    public boolean isRunning() {
        if (this.status != NotebookInstanceStatus.IN_SERVICE) {
            return false;
        }

        return true;
    }

    public boolean isStopping() {
        if (this.status != NotebookInstanceStatus.STOPPING) {
            return false;
        }

        return true;
    }

    public boolean isStopped() {
        if (this.status != NotebookInstanceStatus.STOPPED) {
            return false;
        }

        return true;
    }

    public boolean isTerminated() {
        if (this.status != NotebookInstanceStatus.DELETING) {
            return false;
        }

        return true;
    }

    public static SagemakerInstanceData of(String instanceId, List<Tag> tags) {
        return SagemakerInstanceData.builder().instanceId(instanceId).status(NotebookInstanceStatus.PENDING).tags(tags).build();
    }

    @Builder
    private SagemakerInstanceData(String instanceId, NotebookInstanceStatus status, Instant creationTime, Instant lastModifiedTime, List<Tag> tags) {
        this.instanceId = instanceId;
        this.status = status;
        this.creationTime = creationTime;
        this.lastModifiedTime = lastModifiedTime;
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
