package mzc.data.portal.service;

import mzc.data.portal.dto.CloudSandboxInstanceData;
import mzc.data.portal.enums.SandboxType;

public interface CloudSandboxInstanceService {
    CloudSandboxInstanceData.AddCloudSandboxInstanceParam addInstance(CloudSandboxInstanceData.AddCloudSandboxInstanceParam param);

    void startInstance(SandboxType type, long idx);

    void stopInstance(SandboxType type, long idx);

    void terminateInstance(SandboxType type, long idx);

    void syncSandbox();
}
