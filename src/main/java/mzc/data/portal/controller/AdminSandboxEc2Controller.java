package mzc.data.portal.controller;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.service.SandboxModelService;
import mzc.data.portal.service.SandboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/admin/sandbox-EC2")
public class AdminSandboxEc2Controller {

    private SandboxService sandboxService;
    private SandboxModelService sandboxModelService;

    /**
     * 생성자 주입
     * @param sandboxService
     * @param sandboxModelService
     */
    @Autowired
    public AdminSandboxEc2Controller(SandboxService sandboxService, SandboxModelService sandboxModelService){
        this.sandboxService = sandboxService;
        this.sandboxModelService = sandboxModelService;
    }


    @GetMapping("/{idx}")
    public String sandboxInfo(@PathVariable long idx, Model model) {
        SandboxData sandboxData = sandboxService.getSandboxDetailData(idx);
        SandboxModelData sandboxModelDetailData = sandboxModelService.getSandboxDetailSpecImage(sandboxData.getSandboxModelIdx());

        model.addAttribute("sandboxData", sandboxData);
        model.addAttribute("sandboxModelDetailData", sandboxModelDetailData);

        return "admin/sandbox-ec2-info";
    }

    @GetMapping("/sandbox-data")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes getSandboxInfo(@Valid @RequestParam("idx") String index){
        long idx = Long.parseLong(index);
        SandboxData sandboxData = sandboxService.getSandboxDetailData(idx);
        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, sandboxData);
    }

    @PutMapping("/{idx}/status/accept")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sandboxRequestAccept(@PathVariable long idx, @Valid SandboxData.UpdateSandboxParam param, Model model) {
        param.setIdx(idx);
        StringBuffer result = sandboxService.acceptSandbox(param);
        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, result);
    }


    @PutMapping("/{idx}/status/reject")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes sandboxRequestReject(@PathVariable long idx, Model model) {
        sandboxService.rejectSandbox(idx);
        return new SandboxData.SandboxDataCustomRes<>(ApiResponse.OK, null, null);
    }


}
