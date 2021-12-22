package mzc.data.portal.mapper;

import mzc.data.portal.dto.CloudSandboxInstanceData;
import org.springframework.stereotype.Repository;

@Repository
public interface CloudSandboxInstanceMapper {

    CloudSandboxInstanceData get(long idx);

    void add(CloudSandboxInstanceData.AddCloudSandboxInstanceParam param);

    void update(CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam param);

}
