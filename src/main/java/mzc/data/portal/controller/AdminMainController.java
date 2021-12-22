package mzc.data.portal.controller;

import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.SandboxData;
import mzc.data.portal.dto.SandboxModelData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.service.SandboxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminMainController {

    private SandboxService sandboxService;

    /**
     * 생성자 주입
     * @param sandboxService
     */
    @Autowired
    public AdminMainController(SandboxService sandboxService){
        this.sandboxService = sandboxService;
    }

    @Deprecated
    @GetMapping("")
    public String home(Model model) {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("dashboard")
    public String dashboard(Model model) {

        long sandboxCreatedCount = sandboxService.getSandboxCount("created");
        long sandboxRunningCount = sandboxService.getSandboxCount("running");
        long sandboxStoppedCount = sandboxService.getSandboxCount("stopped");

        model.addAttribute("sandboxCreatedCount", sandboxCreatedCount);
        model.addAttribute("sandboxRunningCount", sandboxRunningCount);
        model.addAttribute("sandboxStoppedCount", sandboxStoppedCount);

        return "admin/dashboard";
    }

    @GetMapping("dashboardOps")
    public String dashboardOps(Model model) { return "admin/dashboard-ops"; }
}
