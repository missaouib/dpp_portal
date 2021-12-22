package mzc.data.portal.agent.service;

import mzc.data.portal.agent.dto.SagemakerInstanceData;
import mzc.data.portal.dto.SagemakerData;
import mzc.data.portal.dto.SandboxData;
import software.amazon.awssdk.services.sagemaker.model.NotebookInstanceSummary;

import java.util.List;


public interface SagemakerService {

    List<SagemakerData> sagemakerList();

    //void addSagemaker(SagemakerData.AddSagemakerParam param);

    void create(SagemakerData.CreateSagemakerInstanceParam param);

    List<NotebookInstanceSummary> sagemakerBookList(List<String> instanceIds);

    String createNotebookPresignedUrl(String cloudInstanceId);

    List<SagemakerInstanceData> list();

    void start(String instanceId);

    void stop(String instanceId);

    void delete(String instanceId);

    void update(SagemakerData.UpdateSagemakerInstanceParam param);
}
