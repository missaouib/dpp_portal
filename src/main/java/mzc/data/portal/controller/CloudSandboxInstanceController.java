package mzc.data.portal.controller;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.CloudSandboxInstanceData;
import mzc.data.portal.enums.SandboxType;
import mzc.data.portal.service.CloudSandboxInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/cloud-instance")
public class CloudSandboxInstanceController {

    private CloudSandboxInstanceService cloudSandboxInstanceService;

    /**
     * 생성자 주입
     * @param cloudSandboxInstanceService
     */
    @Autowired
    public CloudSandboxInstanceController(CloudSandboxInstanceService cloudSandboxInstanceService){
        this.cloudSandboxInstanceService = cloudSandboxInstanceService;
    }

    @PostMapping("/sync")
    @ResponseBody
    public CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes syncSandbox() {

        cloudSandboxInstanceService.syncSandbox();
        return new CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/ec2/start")
    @ResponseBody
    public CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes startInstance(@Valid CloudSandboxInstanceData.UpdateStatusCloudSandboxInstanceParam param, Model model) {
        cloudSandboxInstanceService.startInstance(SandboxType.EC2, param.getIdx());
        return new CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/ec2/stop")
    @ResponseBody
    public CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes stopInstance(@Valid CloudSandboxInstanceData.UpdateStatusCloudSandboxInstanceParam param, Model model) {
        cloudSandboxInstanceService.stopInstance(SandboxType.EC2, param.getIdx());
        return new CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes<>(ApiResponse.OK, null, null);
    }


    @PostMapping("/sagemaker/start")
    @ResponseBody
    public CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes startSagemaker(@Valid CloudSandboxInstanceData.UpdateStatusCloudSandboxInstanceParam param, Model model) {
        cloudSandboxInstanceService.startInstance(SandboxType.SAGEMAKER, param.getIdx());
        return new CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/sagemaker/stop")
    @ResponseBody
    public CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes stopSagemaker(@Valid CloudSandboxInstanceData.UpdateStatusCloudSandboxInstanceParam param, Model model) {
        cloudSandboxInstanceService.stopInstance(SandboxType.SAGEMAKER, param.getIdx());
        return new CloudSandboxInstanceData.CloudSandboxInstanceDataCustomRes<>(ApiResponse.OK, null, null);
    }
}
