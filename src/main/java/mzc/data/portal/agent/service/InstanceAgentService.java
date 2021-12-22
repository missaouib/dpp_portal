package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.InstanceData;

import java.util.List;

public interface InstanceAgentService {

    /**
     * EC2 Instance 정보를 가져온다.
     *
     * @param instanceId
     * @return
     */
    List<InstanceData> get(String instanceId);

    /**
     * EC2 Instance 정보를 조회한다.
     *
     * @param instanceIds
     * @return
     */
    List<InstanceData> list(List<String> instanceIds);

    /**
     * EC2 Instance를 생성한다.
     *
     * @param param
     * @return
     */
    List<InstanceData> create(InstanceData.CreateInstanceParam param);

    /**
     * 중단된 EC2 Instance를 시작한다.
     *
     * @param instanceId
     */
    void start(String instanceId);

    /**
     * EC2 Instance를 중단한다.
     *
     * @param instanceId
     */
    void stop(String instanceId);

    /**
     * EC2 Instance를 종료한다.
     *
     * @param instanceId
     */
    void terminate(String instanceId);
}
