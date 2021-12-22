package mzc.data.portal.controller;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.*;
import mzc.data.portal.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class AdminSandboxController {


    private SandboxService sandboxService;
    private SandboxModelService sandboxModelService;

    /**
     * 생성자 주입
     * @param sandboxService
     * @param sandboxModelService
     */
    @Autowired
    public AdminSandboxController(SandboxService sandboxService, SandboxModelService sandboxModelService){
        this.sandboxService = sandboxService;
        this.sandboxModelService = sandboxModelService;
    }


    @GetMapping("/admin/sandbox")
    public String sandboxList(Model model) {

        List<SandboxData> sandboxes = sandboxService.findSandboxDetailCloudSandboxInstanceList();

        model.addAttribute("sandboxes", sandboxes);

        return "admin/sandbox-list";
    }

    //샌드박스 자동중지 by Hannah
    @GetMapping("/admin/sandbox/form/auto/stop")
    public String autoStopForm(Model model) {

        List<SandboxData> sandboxes = sandboxService.allSandboxes();

        model.addAttribute("sandboxes", sandboxes);

        return "admin/sandbox-auto-stop";
    }

    //샌드박스 자동중지 by Hannah
    @PostMapping("/admin/sandbox/auto/stop")
    @ResponseBody
    public SandboxData.SandboxDataCustomRes autoStop(@RequestParam(value="sandboxList[]") List<String> param) {

        sandboxService.autoStop(param);

        return new SandboxData.SandboxDataCustomRes(ApiResponse.OK, null, null);
    }
}
