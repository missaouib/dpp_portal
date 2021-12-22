package mzc.data.portal.agent.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.config.InstanceConfig;
import mzc.data.portal.agent.dto.InstanceConfigData;
import mzc.data.portal.agent.dto.InstanceData;
import mzc.data.portal.agent.service.InstanceAgentService;
import mzc.data.portal.enums.SandboxOS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InstanceAgentServiceImpl extends CommonEc2AgentServiceImpl implements InstanceAgentService {

    private InstanceConfig instanceConfig;

    /**
     * 생성자 주입
     * @param instanceConfig
     */
    @Autowired
    public InstanceAgentServiceImpl(InstanceConfig instanceConfig){
        this.instanceConfig = instanceConfig;
    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public List<InstanceData> get(String instanceId) {
        log.debug("get: {}", instanceId);

        try (Ec2Client ec2Client = createEc2Client()) {

            DescribeInstancesRequest request = DescribeInstancesRequest.builder().instanceIds(instanceId).build();
            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            log.debug("GetInstanceResponse: {}", response);

            // Instance 정보는 Reservation 내에 들어있다.
            List<Reservation> reservations = response.reservations();

            if (reservations == null || reservations.isEmpty()) {
                throw new IllegalArgumentException("Reservation does not exist");
            }

            List<InstanceData> instanceDataList = new ArrayList<>();

            reservations.forEach(reservation -> {
                if (reservation.instances() == null || reservation.instances().isEmpty()) {
                    throw new IllegalArgumentException("Instance does not exist");
                }
                List<Instance> instances = reservation.instances();
                instanceDataList.addAll(instances.stream().map(this::convert).collect(Collectors.toList()));
            });

            return instanceDataList;
        }
    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public List<InstanceData> create(InstanceData.CreateInstanceParam param) {
        log.debug("create: {}", param);

        String amiId = param.getAmiId();
        String instanceType = param.getInstanceType();
        SandboxOS instanceOs = param.getInstanceOs();
        int volume = param.getEbsVolume();
        List<Tag> tags = convertTags(param.getCloudTags());
        InstanceConfigData instanceConfigData = param.getInstanceConfigData();

        try (Ec2Client ec2Client = createEc2Client()) {
            //태그값을 설정한다.
            TagSpecification tagSpecification = TagSpecification.builder().tags(tags).resourceType(ResourceType.INSTANCE).build();

            //서브넷 아이
            String subnetId = instanceConfig.getInstanceSubnetId();

            //보안그룹
            String securityGroups = instanceConfig.getInstanceSecurityGroup();

            //EBS Device 이
            String deviceName = instanceConfig.getInstanceDeviceName();

            //EBS 설정
            EbsBlockDevice ebsBlockDevice = EbsBlockDevice.builder().deleteOnTermination(true).volumeType(VolumeType.GP2).volumeSize(volume).build();
            BlockDeviceMapping blockDeviceMapping = BlockDeviceMapping.builder().deviceName(deviceName).ebs(ebsBlockDevice).build();


            String userData = "";
            // 샌드박스 OS별 유저 생성
            if (instanceOs == SandboxOS.LINUX) {
                //securityGroups = instanceConfigData.getLinuxSecurityGroup();
                userData = getLinuxUserData(instanceConfigData);
            }

            log.error("test");

            //Instance 생성
            RunInstancesRequest runRequest = RunInstancesRequest.builder()
                    .imageId(amiId)
                    .instanceType(instanceType)
                    .maxCount(1)
                    .minCount(1)
                    .subnetId(subnetId)
                    .tagSpecifications(tagSpecification)
                    .securityGroupIds(securityGroups)
                    .blockDeviceMappings(blockDeviceMapping)
                    .userData(userData)
                    .build();

            RunInstancesResponse response = ec2Client.runInstances(runRequest);

            log.debug("RunInstanceResponse: {}", response);

            List<Instance> instances = response.instances();

            return instances.stream().map(i -> InstanceData.of(i.instanceId(), i.tags())).collect(Collectors.toList());

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public List<InstanceData> list(List<String> instanceIds) {
        log.debug("list: {}", instanceIds);

        try (Ec2Client ec2Client = createEc2Client()) {

            DescribeInstancesRequest request = DescribeInstancesRequest.builder().instanceIds(instanceIds).build();
            DescribeInstancesResponse response = ec2Client.describeInstances(request);

            log.debug("GetInstanceResponse: {}", response);

            // Instance 정보는 Reservation 내에 들어있다.
            List<Reservation> reservations = response.reservations();

            if (reservations == null || reservations.isEmpty()) {
                throw new IllegalArgumentException("Reservation does not exist");
            }

            List<InstanceData> instanceDataList = new ArrayList<>();

            reservations.forEach(reservation -> {
                if (reservation.instances() == null || reservation.instances().isEmpty()) {
                    throw new IllegalArgumentException("Instance does not exist");
                }
                List<Instance> instances = reservation.instances();
                instanceDataList.addAll(instances.stream().map(this::convert).collect(Collectors.toList()));
            });

            return instanceDataList;
        }
    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public void start(String instanceId) {

        try (Ec2Client ec2Client = createEc2Client()) {
            StartInstancesRequest request = StartInstancesRequest.builder().instanceIds(instanceId).build();
            StartInstancesResponse response = ec2Client.startInstances(request);
            System.out.println(response);
        }catch(Exception e){

        }

    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public void stop(String instanceId) {

        try (Ec2Client ec2Client = createEc2Client()) {
            StopInstancesRequest request = StopInstancesRequest.builder().instanceIds(instanceId).build();
            ec2Client.stopInstances(request);
        }
    }

    @Override
    @Retryable(value = AwsServiceException.class, backoff = @Backoff(delay = 200))
    public void terminate(String instanceId) {

        try (Ec2Client ec2Client = createEc2Client()) {
            TerminateInstancesRequest request = TerminateInstancesRequest.builder().instanceIds(instanceId).build();
            ec2Client.terminateInstances(request);
        } catch (Ec2Exception e) {
        }
    }

    private InstanceData convert(Instance instance) {
        InstanceData instanceData = new InstanceData();
        instanceData.setInstanceId(instance.instanceId());
        instanceData.setEbsId(instance.blockDeviceMappings());
        instanceData.setPublicDns(instance.publicDnsName());
        instanceData.setPublicIp(instance.publicIpAddress());
        instanceData.setPrivateIp(instance.privateIpAddress());
        instanceData.setStatus(instance.state().name());
        instanceData.setStartedAt(instance.launchTime());
        instanceData.setTags(instance.tags());
        return instanceData;
    }

    private static Ec2Client createEc2Client() {
        return Ec2Client.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2).build();
    }


    private String getLinuxUserData(InstanceConfigData instanceConfigData) {

        StringBuffer sb = new StringBuffer("#!/bin/bash" + "\n");
        sb.append("sudo useradd -m -s /bin/bash -G sudo " + instanceConfigData.getLinuxUserId() + "\n");
        sb.append("echo '" + instanceConfigData.getLinuxUserId() + ":" + instanceConfigData.getLinuxUserPasswd() + "' | sudo chpasswd");

        try {
            byte[] targetBytes = String.valueOf(sb).getBytes("UTF-8");
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode(targetBytes);

            return new String(encodedBytes,"UTF-8");
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
