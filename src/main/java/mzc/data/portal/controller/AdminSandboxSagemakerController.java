package mzc.data.portal.controller;


import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.agent.service.SagemakerService;
import mzc.data.portal.dto.*;
import mzc.data.portal.service.SandboxModelService;
import mzc.data.portal.service.SandboxSagemakerService;
import mzc.data.portal.service.SandboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/sandbox-SAGEMAKER")
public class AdminSandboxSagemakerController {

    private SandboxService sandboxService;
    private SandboxSagemakerService sandboxSagemakerService;
    private SandboxModelService sandboxModelService;
    private SagemakerService sagemakerService;

    /**
     * 생성자 주입
     * @param sandboxService
     * @param sandboxSagemakerService
     * @param sandboxModelService
     * @param sagemakerService
     */
    @Autowired
    public AdminSandboxSagemakerController(SandboxService sandboxService,SandboxSagemakerService sandboxSagemakerService,SandboxModelService sandboxModelService, SagemakerService sagemakerService){
        this.sandboxService = sandboxService;
        this.sandboxSagemakerService = sandboxSagemakerService;
        this.sandboxModelService = sandboxModelService;
        this.sagemakerService = sagemakerService;
    }

    @GetMapping("/{idx}")
    public String sandboxInfo(@PathVariable long idx, Model model) {
        SandboxData sandboxData = sandboxSagemakerService.getSandboxDetailData(idx);
        SandboxModelData sandboxModelDetailData = sandboxModelService.getSandboxDetailSpecImage(sandboxData.getSandboxModelIdx());

        model.addAttribute("sandboxData", sandboxData);
        model.addAttribute("sandboxModelDetailData", sandboxModelDetailData);

        return "admin/sandbox-sagemaker-info";
    }

    @PutMapping("/{idx}/status/accept")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sandboxRequestAccept(@PathVariable long idx, @Valid SandboxData.UpdateSandboxParam param, Model model) {
        param.setIdx(idx);
        sandboxSagemakerService.acceptSandbox(param);
        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, null);
    }


    @PutMapping("/{idx}/status/reject")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sandboxRequestReject(@PathVariable long idx, Model model) {
        sandboxSagemakerService.rejectSandbox(idx);
        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @PostMapping("/presign-url")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sagemakerUrl(@Valid SandboxData.SandboxSagemakerParam param, Model model) {

        SandboxData sandboxData = sandboxSagemakerService.getSandboxDetailData(param.getIdx());
        String url = sagemakerService.createNotebookPresignedUrl(sandboxData.getCloudInstanceId());


        model.addAttribute("url", url);

        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, url);
    }

}
