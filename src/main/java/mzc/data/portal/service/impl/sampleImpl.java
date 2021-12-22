package mzc.data.portal.service.impl;

import mzc.data.portal.agent.dto.InstanceData;
import mzc.data.portal.dto.SandboxData;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.sagemaker.SageMakerClient;
import software.amazon.awssdk.services.sagemaker.model.*;

import java.util.List;


public class sampleImpl {

//    sample region 예시
//    Region region = Region.US_WEST_2;
    public void test() {
        SageMakerClient sageMakerClient = SageMakerClient.builder()
                .region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2)
                .build();

//        addBooks(sageMakerClient);

        listBooks(sageMakerClient);

        sageMakerClient.close();
    }

    private static SageMakerClient createSageMakerClient() {
        return SageMakerClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_2).build();
    }

    public void addBooks(SandboxData.AddNotebookParam param) {

        try (SageMakerClient sageMakerClient = createSageMakerClient()) {
            //createNotebookInstanceRequest
            String notebookInstanceName = param.getNotebookInstanceName();
            String instanceType = param.getInstanceType();

            CreateNotebookInstanceRequest request = CreateNotebookInstanceRequest.builder()
                    .notebookInstanceName(notebookInstanceName) //instanceName은 사용자에게 입력받은 값으로 진행
                    .instanceType(instanceType) //instanceName은 사용자에게 입력받은 값으로 진행
                    .securityGroupIds("a")
                    .roleArn("a")
                    .kmsKeyId("a")
//                    .tags()
                    .lifecycleConfigName("a")
                    .directInternetAccess("a")
                    .volumeSizeInGB(256)
//                    .acceleratorTypes("a")
                    .defaultCodeRepository("a")
                    .additionalCodeRepositories("a")
                    .rootAccess("a")
                    .build();

            // add a notebook
            CreateNotebookInstanceResponse response = sageMakerClient.createNotebookInstance(request);

        } catch (SageMakerException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
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
}
