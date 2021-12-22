package mzc.data.portal.controller;

import mzc.data.portal.agent.service.SagemakerService;
import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.service.SandboxSagemakerService;
import mzc.data.portal.service.SandboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SandboxController {


    private SandboxService sandboxService;
    private SandboxSagemakerService sandboxSagemakerService;
    private SagemakerService sagemakerService;

    /**
     * 생성자 주입
     * @param sandboxService
     * @param sandboxSagemakerService
     * @param sagemakerService
     */
    @Autowired
    public SandboxController(SandboxService sandboxService, SandboxSagemakerService sandboxSagemakerService, SagemakerService sagemakerService){
        this.sandboxService = sandboxService;
        this.sandboxSagemakerService = sandboxSagemakerService;
        this.sagemakerService = sagemakerService;
    }


    @GetMapping("/sandbox/form/create")
    public String addSandboxForm(Model model) {

        //EC2 샌드박스 모델 조회
        List<SandboxData.Models> ec2SandboxModels = sandboxService.ec2SandboxModels();
        //Sagemaker 샌드박스 모델 조회
        List<SandboxData.Models> sagemakerSandboxModels = sandboxService.sagemakerSandboxModels();

        model.addAttribute("ec2SandboxModels", ec2SandboxModels);
        model.addAttribute("sagemakerSandboxModels", sagemakerSandboxModels);
        return "user/sandbox-add";
    }


    //샌드박스 "생성 신청" 사용자가 -> 관리자에게
    @PostMapping("/sandbox/add")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes addSandbox(@Valid SandboxData.AddSandboxParam param) {

        sandboxService.addSandbox(param);

        return new SandboxData.SandboxDataCustomRes(ApiResponse.CREATED, null, null);
    }

    @GetMapping("/sandboxes")
    public String sandboxes(Model model) {

        List<SandboxData> sandboxes = sandboxService.sandboxes();

        model.addAttribute("sandboxes", sandboxes);

        return "user/sandbox-list";
    }

    //샌드박스 "종료 신청" 사용자가 -> 관리자에게
    @PostMapping("/sandbox/request/terminate")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes terminateSandbox(@Valid SandboxData.TerminateSandboxParam param) {

        sandboxService.terminateSandbox(param);

        return new SandboxData.SandboxDataCustomRes(ApiResponse.CREATED, null, null);
    }

    @PostMapping("/sandboxes/presign-url")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sagemakerUrl(@Valid SandboxData.SandboxSagemakerParam param, Model model) {

        SandboxData sandboxData = sandboxSagemakerService.getSandboxDetailData(param.getIdx());
        String url = sagemakerService.createNotebookPresignedUrl(sandboxData.getCloudInstanceId());


        model.addAttribute("url", url);

        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, url);
    }

}
