package mzc.data.portal.service.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.dto.InstanceConfigData;
import mzc.data.portal.agent.dto.InstanceData;
import mzc.data.portal.agent.dto.SagemakerInstanceData;
import mzc.data.portal.agent.dto.TagData;
import mzc.data.portal.agent.service.Ec2TagAgentService;
import mzc.data.portal.agent.service.InstanceAgentService;
import mzc.data.portal.agent.service.SagemakerService;
import mzc.data.portal.dto.*;
import mzc.data.portal.enums.CloudSandboxInstanceStatus;
import mzc.data.portal.enums.SandboxOS;
import mzc.data.portal.enums.SandboxStatus;
import mzc.data.portal.enums.SandboxType;
import mzc.data.portal.mapper.CloudSandboxInstanceMapper;
import mzc.data.portal.mapper.SandboxMapper;
import mzc.data.portal.service.CloudSandboxInstanceService;
import mzc.data.portal.service.SandboxService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@ComponentScan
public class CloudSandboxInstanceServiceImpl extends BaseServiceImpl implements CloudSandboxInstanceService {


    private InstanceAgentService instanceAgentService;
    private SagemakerService sagemakerService;
    private CloudSandboxInstanceMapper cloudSandboxInstanceMapper;
    private SandboxMapper sandboxMapper;

    @Autowired
    public CloudSandboxInstanceServiceImpl(InstanceAgentService instanceAgentService, SagemakerService sagemakerService, SandboxService sandboxService, Ec2TagAgentService ec2TagAgentService, CloudSandboxInstanceMapper cloudSandboxInstanceMapper,SandboxMapper sandboxMapper){
        this.instanceAgentService = instanceAgentService;
        this.sagemakerService = sagemakerService;
        this.cloudSandboxInstanceMapper = cloudSandboxInstanceMapper;
        this.sandboxMapper = sandboxMapper;

    }


    @Override
    @Transactional
    public CloudSandboxInstanceData.AddCloudSandboxInstanceParam addInstance(CloudSandboxInstanceData.AddCloudSandboxInstanceParam param) {
        Assert.notNull(param, "AddCloudSandboxInstanceParam must be not null");

        long sandboxIdx = param.getIdx();
        String instanceUuid = UUID.randomUUID().toString();

        SandboxData sandboxData = null;
        List<InstanceData> instanceDataList = null;

        UserData userData = getUser();

        SandboxType sandboxType = param.getSandboxType();

        try {
            if(sandboxIdx < 1) {
                throw new IllegalArgumentException("sandboxIdx is Null");
            }

            if(sandboxType == SandboxType.EC2) {

                String tagNameValue = "dpp-sandbox";
                TagData tagData = TagData.of("Name", tagNameValue);
                List<TagData> cloudTags = new ArrayList<>();
                cloudTags.add(tagData);
                cloudTags = addTagKeySandbox(cloudTags);

                InstanceConfigData instanceConfigData = new InstanceConfigData();
                instanceConfigData.setLinuxUserId("sandboxUser");
                instanceConfigData.setLinuxUserPasswd("sandbox!@");

                InstanceData.CreateInstanceParam createInstanceParam =
                        InstanceData.CreateInstanceParam.builder()
                                .amiId(param.getAmiId())
                                .instanceType(param.getInstanceType())
                                .EbsVolume(param.getInstanceVolume())
                                .instanceOs(SandboxOS.LINUX)
                                .cloudTags(cloudTags)
                                .instanceConfigData(instanceConfigData)
                                .build();

                //CloudAgent에 인스턴스 호출 (instanceUuid를 리턴으로 받음)
                instanceDataList = instanceAgentService.create(createInstanceParam);
                String instanceId = instanceDataList.get(0).getInstanceId();

                String tagJson = new Gson().toJson(cloudTags);

                param.setInstanceUuid(instanceUuid);
                param.setInstanceId(instanceId);
                param.setCloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STARTING);
                param.setSandboxType(SandboxType.EC2);

                cloudSandboxInstanceMapper.add(param);

                SandboxData.UpdateSandboxParam updateSandboxParam = SandboxData.UpdateSandboxParam.builder().idx(sandboxIdx).userIdx(userData.getIdx()).sandboxStatus(SandboxStatus.ACTIVE).instanceUuid(instanceUuid).build();

                sandboxMapper.updateSandbox(updateSandboxParam);
            } else {

                String tagNameValue = "dpp-sandbox";
                TagData tagData = TagData.of("Name", tagNameValue);
                List<TagData> cloudTags = new ArrayList<>();
                cloudTags.add(tagData);
                cloudTags = addTagKeySandbox(cloudTags);

                //String instanceName = param.getInstanceId();
                String instanceName = "dpp-dev-"+param.getIdx();

                SagemakerData.CreateSagemakerInstanceParam createSagemakerInstanceParam =
                        SagemakerData.CreateSagemakerInstanceParam.builder()
                                .instanceName(instanceName)
                                .instanceType(param.getInstanceType())
                                .cloudTags(cloudTags)
                                .EbsVolume(param.getInstanceVolume())
                                .build();

                sagemakerService.create(createSagemakerInstanceParam);
                param.setInstanceUuid(instanceUuid);
                param.setInstanceId(instanceName);
                param.setCloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STARTING);
                param.setSandboxType(SandboxType.SAGEMAKER);

                cloudSandboxInstanceMapper.add(param);

                SandboxData.UpdateSandboxParam updateSandboxParam = SandboxData.UpdateSandboxParam.builder().idx(sandboxIdx).userIdx(userData.getIdx()).sandboxStatus(SandboxStatus.ACTIVE).instanceUuid(instanceUuid).build();
                
                sandboxMapper.updateSandbox(updateSandboxParam);

            }

            return param;
            //TODO service 로 변경




        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //CloudSandbox 생성시 문제가 발생하면 생성된 Cloud 자원은 모두 삭제
            //Cloud 자원 삭제시 오류가 발생하면 무시
            try {
                if (instanceDataList != null) {
                    instanceDataList.forEach(instanceData -> {
                        terminate(instanceData.getInstanceId());
                    });
                }
            } catch (Ec2Exception ignored) {
            }

            throw new IllegalStateException(e.getMessage());
        }

    }

    @Override
    public void startInstance(SandboxType type, long idx) {
        CloudSandboxInstanceData cloudSandboxInstanceData = cloudSandboxInstanceMapper.get(idx);

        String instanceId = null;
        instanceId = cloudSandboxInstanceData.getInstanceId();

        CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam updateCloudSandboxInstanceParam = null;
        try {
            if(instanceId == null) {
                throw new NotFoundException("instanceId is not null");
            }

            if(type == SandboxType.EC2) {
                instanceAgentService.start(instanceId);
            } else {
                sagemakerService.start(instanceId);
            }
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STARTING).isRunning(true).isStopped(false).isTerminated(false).idx(idx).build();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.ERROR).isRunning(false).isStopped(false).isTerminated(false).idx(idx).build();

        } finally {
            cloudSandboxInstanceMapper.update(updateCloudSandboxInstanceParam);
        }
    }

    @Override
    public void stopInstance(SandboxType type, long idx) {
        CloudSandboxInstanceData cloudSandboxInstanceData = cloudSandboxInstanceMapper.get(idx);

        String instanceId = null;
        instanceId = cloudSandboxInstanceData.getInstanceId();

        CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam updateCloudSandboxInstanceParam = null;
        try {
            if(instanceId == null) {
                throw new NotFoundException("instanceId is not null");
            }
            if(type == SandboxType.EC2) {
                instanceAgentService.stop(instanceId);
            } else {
                sagemakerService.stop(instanceId);
            }
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STOPPED).isRunning(false).isStopped(true).isTerminated(false).idx(idx).build();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.ERROR).isRunning(false).isStopped(false).isTerminated(false).idx(idx).build();

        } finally {
            cloudSandboxInstanceMapper.update(updateCloudSandboxInstanceParam);
        }

    }

    @Override
    public void terminateInstance(SandboxType type, long idx) {
        CloudSandboxInstanceData cloudSandboxInstanceData = cloudSandboxInstanceMapper.get(idx);

        String instanceId = null;
        instanceId = cloudSandboxInstanceData.getInstanceId();

        CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam updateCloudSandboxInstanceParam = null;
        try {
            if(instanceId == null) {
                throw new NotFoundException("instanceId is not null");
            }

            if(type == SandboxType.EC2) {
                instanceAgentService.terminate(instanceId);
            } else {
                sagemakerService.delete(instanceId);
            }
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.TERMINATED).isRunning(false).isStopped(false).isTerminated(true).idx(idx).build();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.ERROR).isRunning(false).isStopped(false).isTerminated(false).idx(idx).build();

        } finally {
            cloudSandboxInstanceMapper.update(updateCloudSandboxInstanceParam);
        }

    }



    @Override
    public void syncSandbox() {
        syncSagemaker();
        syncEc2();
    }

    /**
     * Sagemaker Sync
     */
    public void syncSagemaker() {

        try {
            SandboxType sandboxType = SandboxType.SAGEMAKER;
            List<SandboxData> checklist = sandboxMapper.findCreatedSandboxDetailCloudSandboxInstanceList(sandboxType);
            List<String> instanceIdList = checklist.stream().map(SandboxData::getCloudInstanceId).collect(Collectors.toList());

            List<SagemakerInstanceData> sagemakerInstanceDataList = sagemakerService.list();

            checklist.forEach(checkData -> {
                CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam updateCloudSandboxInstanceParam =
                        CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().build();

                SagemakerInstanceData sagemakerInstanceData =   sagemakerInstanceDataList.stream().filter(i -> i.getInstanceId().equals(checkData.getCloudInstanceId())).findAny().orElse(null);

                if(sagemakerInstanceData == null) {

                    SandboxData.UpdateSandboxParam terminateProjectSandboxParam = SandboxData.UpdateSandboxParam.builder().idx(checkData.getIdx()).sandboxStatus(SandboxStatus.TERMINATED).build();
                    sandboxMapper.updateSandbox(terminateProjectSandboxParam);
                    return;
                }
                if(sagemakerInstanceData != null) {

                    if (sagemakerInstanceData.isPending()) {
                        updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STARTING).isRunning(false).isStopped(false).isTerminated(false).idx(checkData.getCloudIdx()).build();
                    }

                    if (sagemakerInstanceData.isCompleted()) {
                        //인스턴스 StartTime설정
                        LocalDateTime startedAt = LocalDateTime.ofInstant(sagemakerInstanceData.getLastModifiedTime(), ZoneOffset.of("+09:00"));
                        // cloudsandboxinstance 값 업데이트

                        updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder()
                                .cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.RUNNING)
                                .isRunning(true).isStopped(false).isTerminated(false)
                                .idx(checkData.getCloudIdx())
                                .startedAt(startedAt)
                                .build();
                    }

                    if (sagemakerInstanceData.isStopping()) {
                        updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STOPPING).isRunning(false).isStopped(false).isTerminated(false).idx(checkData.getCloudIdx()).build();
                    }

                    if (sagemakerInstanceData.isStopped()) {
                        updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STOPPED).isRunning(false).isStopped(true).isTerminated(false).idx(checkData.getCloudIdx()).build();
                    }

                    if (sagemakerInstanceData.isTerminated()) {
                        updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.TERMINATED).isRunning(false).isStopped(false).isTerminated(true).idx(checkData.getCloudIdx()).build();
                    }

                }
                cloudSandboxInstanceMapper.update(updateCloudSandboxInstanceParam);


            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * EC2 Sync
     */
    public void syncEc2() {

        try {

        // 상태 검사할 데이터 가져오기
        SandboxType sandboxType = SandboxType.EC2;
        List<SandboxData> checklist = sandboxMapper.findCreatedSandboxDetailCloudSandboxInstanceList(sandboxType);

        // 인스턴스 List 추출
        List<String> instanceIdList = checklist.stream().map(SandboxData::getCloudInstanceId).collect(Collectors.toList());

        // 인스턴스 조회
        List<InstanceData> instanceDataList = instanceAgentService.list(instanceIdList);

        // for에 인스턴스 매칭되면 값 업데이트 빌더 처리 매칭안되면 TERMINATED나 ERROR처리
        //List<CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam> updateCloudSandboxInstanceParamList = new ArrayList<>();


        checklist.forEach(checkData -> {
            // 값 초기화 TERMINATED 상태로

            CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam updateCloudSandboxInstanceParam =
                    CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().build();

            // 인스턴스 있는지 체크
//                boolean isCreatedInstance = instanceDataList.stream().anyMatch(i -> i.getInstanceId().equals(checkData.getInstanceId()));
            InstanceData instanceData = instanceDataList.stream().filter(i -> i.getInstanceId().equals(checkData.getCloudInstanceId())).findAny().orElse(null);

            // 없으면 이미 종료된거라 삭제 처리
            if (instanceData == null) {
                SandboxData.UpdateSandboxParam terminateProjectSandboxParam = SandboxData.UpdateSandboxParam.builder().idx(checkData.getIdx()).sandboxStatus(SandboxStatus.TERMINATED).build();
                sandboxMapper.updateSandbox(terminateProjectSandboxParam);
            }

            // STARTING 및 RUNNING 및 STOPPED 체크
            if (instanceData != null) {

                if (instanceData.isPending()) {
                    updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STARTING).isRunning(false).isStopped(false).isTerminated(false).idx(checkData.getCloudIdx()).build();
                }

                if (instanceData.isCompleted()) {
                    //인스턴스 StartTime설정
                    LocalDateTime startedAt = LocalDateTime.ofInstant(instanceData.getStartedAt(), ZoneOffset.of("+09:00"));
                    // cloudsandboxinstance 값 업데이트

                    String connectionPrivateUrl = instanceData.getPrivateIp();
                    String connectionPublicUrl = instanceData.getPublicIp();
                    updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder()
                            .cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.RUNNING)
                            .isRunning(true).isStopped(false).isTerminated(false)
                            .connectionPrivateUrl(connectionPrivateUrl)
                            .connectionPublicUrl(connectionPublicUrl)
                            .idx(checkData.getCloudIdx())
                            .startedAt(startedAt)
                            .build();
                }

                if (instanceData.isStopping()) {
                    updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STOPPING).isRunning(false).isStopped(false).isTerminated(false).idx(checkData.getCloudIdx()).build();
                }

                if (instanceData.isStopped()) {
                    updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.STOPPED).isRunning(false).isStopped(true).isTerminated(false).idx(checkData.getCloudIdx()).build();
                }

                if (instanceData.isTerminated()) {
                    updateCloudSandboxInstanceParam = CloudSandboxInstanceData.UpdateCloudSandboxInstanceParam.builder().cloudSandboxInstanceStatus(CloudSandboxInstanceStatus.TERMINATED).isRunning(false).isStopped(false).isTerminated(true).idx(checkData.getCloudIdx()).build();
                }
            }

            cloudSandboxInstanceMapper.update(updateCloudSandboxInstanceParam);
        });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void terminate(String instanceId) {
        Assert.notNull(instanceId, "instanceId must be not null");

        try {
            instanceAgentService.terminate(instanceId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<TagData> addTagKeySandbox(List<TagData> cloudTags) {
        TagData tagData = new TagData();
        tagData.setKey("Sandbox");
        tagData.setValue("Instance");
        cloudTags.add(tagData);

        return cloudTags;
    }



}
