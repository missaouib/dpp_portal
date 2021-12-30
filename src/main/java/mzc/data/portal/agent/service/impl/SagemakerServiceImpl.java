package mzc.data.portal.agent.service.impl;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.config.InstanceConfig;
import mzc.data.portal.agent.dto.SagemakerInstanceData;
import mzc.data.portal.dto.SagemakerData;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.mapper.CloudSandboxInstanceMapper;
import mzc.data.portal.mapper.SagemakerMapper;
import mzc.data.portal.mapper.SandboxMapper;
import mzc.data.portal.agent.service.SagemakerService;
import mzc.data.portal.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sagemaker.SageMakerClient;
import software.amazon.awssdk.services.sagemaker.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@ComponentScan
public class SagemakerServiceImpl extends BaseServiceImpl implements SagemakerService {

    private InstanceConfig instanceConfig;
    private SagemakerMapper sagemakerMapper;
    private SandboxMapper sandboxMapper;
    private CloudSandboxInstanceMapper cloudSandboxInstanceMapper;
    private SandboxData sandboxData;

    /**
     * 생성자 주입
     * @param instanceConfig
     * @param sagemakerMapper
     * @param sandboxMapper
     * @param cloudSandboxInstanceMapper
     * @param sandboxData
     */
    @Autowired
    public SagemakerServiceImpl(InstanceConfig instanceConfig, SagemakerMapper sagemakerMapper, SandboxMapper sandboxMapper, CloudSandboxInstanceMapper cloudSandboxInstanceMapper){
        this.instanceConfig = instanceConfig;
        this.sagemakerMapper = sagemakerMapper;
        this.sandboxMapper = sandboxMapper;
        this.cloudSandboxInstanceMapper = cloudSandboxInstanceMapper;
    }


    @Override
    public List<SagemakerData> sagemakerList() {
        List<SagemakerData> sagemakerList = new ArrayList<>();
        try {
            sagemakerList = sagemakerMapper.sagemakerList();
        } catch (Exception e) {
//            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        } return sagemakerList;
    }

//    @Override
//    public void addSagemaker(SagemakerData.AddSagemakerParam param) {
//        try {
//
//            sagemakerMapper.addSagemaker(param);
//        } catch (Exception e) {
////            log.error(e.getMessage());
//            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
//        }
//    }

    //    sample region 예시
//    Region region = Region.US_WEST_2;
/*
    public void addSagemaker(SagemakerData.AddSagemakerParam param) {
        SageMakerClient sageMakerClient = SageMakerClient.builder()
                .region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2)
                .build();

        addBooks(param);

//        listBooks(sageMakerClient);

//        sageMakerClient.close();
    }
*/

    private static SageMakerClient createSageMakerClient() {
        return SageMakerClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2).build();
    }

    public void create(SagemakerData.CreateSagemakerInstanceParam param) {

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            //createNotebookInstanceRequest
//            String notebookInstanceName = param.getNotebookInstanceName();
//            String instanceType = param.getInstanceType();
            String notebookInstanceName = param.getInstanceName();
            String instanceType = param.getInstanceType();

            //보안그룹
            String securityGroups = instanceConfig.getInstanceSecurityGroup();
            String roleArn = "arn:aws:iam::504042098950:role/AmazonSageMaker-ExecutionRole-20210319T212361";

            CreateNotebookInstanceRequest request = CreateNotebookInstanceRequest.builder()
                    .notebookInstanceName(notebookInstanceName) //instanceName은 사용자에게 입력받은 값으로 진행
                    .instanceType(instanceType) //instanceName은 사용자에게 입력받은 값으로 진행
//                    .securityGroupIds(securityGroups)
                    .roleArn(roleArn)
//                    .kmsKeyId("a")
//                    .tags()
//                    .lifecycleConfigName("a")
//                    .directInternetAccess("a")
//                    .volumeSizeInGB(256)
//                    .acceleratorTypes("a")
//                    .defaultCodeRepository("a")
//                    .additionalCodeRepositories("a")
//                    .rootAccess("a")
                    .build();

            // add a notebook
            CreateNotebookInstanceResponse response = sageMakerClient.createNotebookInstance(request);
            System.out.println(response);

        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    public List<SagemakerInstanceData> list() {
        List<SagemakerInstanceData> sagemakerInstanceData = new ArrayList<>();

        List<NotebookInstanceSummary> items = new ArrayList<>();

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            ListNotebookInstancesRequest request = ListNotebookInstancesRequest.builder().build();
            ListNotebookInstancesResponse notebookInstancesResponse = sageMakerClient.listNotebookInstances();
            items = notebookInstancesResponse.notebookInstances();

            sagemakerInstanceData.addAll(items.stream().map(this::convert).collect(Collectors.toList()));

            /*for (NotebookInstanceSummary item: items) {
                sagemakerInstanceData.addAll(item.stream().map(this::convert).collect(Collectors.toList()));

            }*/

        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }

        return sagemakerInstanceData;
    }

    public List<NotebookInstanceSummary> sagemakerBookList(List<String> instanceIds) {

        List<NotebookInstanceSummary> items = new ArrayList<>();

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            ListNotebookInstancesRequest request = ListNotebookInstancesRequest.builder().build();
            ListNotebookInstancesResponse notebookInstancesResponse = sageMakerClient.listNotebookInstances();
            items = notebookInstancesResponse.notebookInstances();

            for (NotebookInstanceSummary item: items) {
                //System.out.println("The notebook name is: "+item.notebookInstanceName());
            }

        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return items;
    }

    public List<NotebookInstanceSummary> listBooks(SageMakerClient sageMakerClient) {
        // Get a list of notebooks
        ListNotebookInstancesResponse notebookInstancesResponse = sageMakerClient.listNotebookInstances();
        List<NotebookInstanceSummary> items = notebookInstancesResponse.notebookInstances();

        try {
            for (NotebookInstanceSummary item: items) {
                //println으로 출력해주는 경우
                System.out.println("The notebook name is: "+item.notebookInstanceName());
            }
            //TODO return 해주어서 front에 데이터를 띄어주어야 하는 로직
        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        } return items;
    }

    @Override
    public void start(String instanceId) {

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            StartNotebookInstanceRequest request = StartNotebookInstanceRequest
                    .builder()
                    .notebookInstanceName(instanceId)
                    .build();

            StartNotebookInstanceResponse response = sageMakerClient.startNotebookInstance(request);

        }
    }

    @Override
    public void stop(String instanceId) {
        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            StopNotebookInstanceRequest request = StopNotebookInstanceRequest.builder()
                    .notebookInstanceName(instanceId)
                    .build();

            StopNotebookInstanceResponse response = sageMakerClient.stopNotebookInstance(request);
        }
    }

    /**
     * sagemaker 삭제
     * MUST status IN (Stopped, Failed)
     * @param instanceId
     */
    public void delete(String instanceId) {
        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            DeleteNotebookInstanceRequest request = DeleteNotebookInstanceRequest.builder()
                    .notebookInstanceName(instanceId)
                    .build();

            DeleteNotebookInstanceResponse response = sageMakerClient.deleteNotebookInstance(request);

        }
    }

    @Override
    public String createNotebookPresignedUrl(String cloudInstanceId) {

        //String url = sandboxMapper.findSandboxDetailCloudSandboxInstance(idx);
        //CloudSandboxInstanceData cloudSandboxInstanceData = cloudSandboxInstanceMapper.get(idx);
        //log.error("test");
        String url = cloudInstanceId;
        try (SageMakerClient sageMakerClient = createSageMakerClient()) {

            CreatePresignedNotebookInstanceUrlRequest request = CreatePresignedNotebookInstanceUrlRequest.builder()
                    /*.notebookInstanceName("testsgname")*/
                    .notebookInstanceName(url)
                    .sessionExpirationDurationInSeconds(1800)
                    .build();

            CreatePresignedNotebookInstanceUrlResponse response = sageMakerClient.createPresignedNotebookInstanceUrl(request);

            return response.authorizedUrl();

        }
    }

    @Override
    public void update(SagemakerData.UpdateSagemakerInstanceParam param) {

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {

            String instanceName = param.getInstanceName();
            String instanceType = param.getInstanceType();
            int volume  = param.getEbsVolume();

            UpdateNotebookInstanceRequest request = UpdateNotebookInstanceRequest.builder()
                    .notebookInstanceName("dpp-dev-8")
                    .instanceType("ml.t3.medium")
                    .volumeSizeInGB(8)
                    .build();

            UpdateNotebookInstanceResponse response = sageMakerClient.updateNotebookInstance(request);

        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }

    private SagemakerInstanceData convert(NotebookInstanceSummary notebook) {
        SagemakerInstanceData sagemakerInstanceData = new SagemakerInstanceData();
        sagemakerInstanceData.setInstanceId(notebook.notebookInstanceName());
        sagemakerInstanceData.setStatus(notebook.notebookInstanceStatus());
        sagemakerInstanceData.setCreationTime(notebook.creationTime());
        sagemakerInstanceData.setArn(notebook.notebookInstanceArn());
        sagemakerInstanceData.setUrl(notebook.url());
        sagemakerInstanceData.setLastModifiedTime(notebook.lastModifiedTime());
        //instanceData.setTags(notebook.tags());
        return sagemakerInstanceData;
    }

}
