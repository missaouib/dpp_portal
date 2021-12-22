package mzc.data.portal.controller;

import mzc.data.portal.dto.UserData;
import mzc.data.portal.service.SandboxService;
import mzc.data.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private UserService userService;
    private SandboxService sandboxService;

    @Autowired
    public MainController(UserService userService, SandboxService sandboxService){
        this.userService = userService;
        this.sandboxService = sandboxService;
    }

    @GetMapping("/main")
    public String main(Model model) {
        return "redirect:/dashboard";
    }

   /* @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "user/dashboard";
    }*/

    @Deprecated
    @GetMapping("/form")
    public String form(Model model) {

        return "user/form";
    }

    @Deprecated
    @GetMapping("/table")
    public String table(Model model) {

        return "user/table";
    }

    @Deprecated
    @GetMapping("/select/test")
    public String selectTest(Model model) {

        return "test/select";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        long userId = userData.getIdx();

        long sandboxCreatedCount = sandboxService.getUserSandboxCount("created", userId);
        long sandboxRunningCount = sandboxService.getUserSandboxCount("running", userId);
        long sandboxStoppedCount = sandboxService.getUserSandboxCount("stopped", userId);


        model.addAttribute("sandboxCreatedCount", sandboxCreatedCount);
        model.addAttribute("sandboxRunningCount", sandboxRunningCount);
        model.addAttribute("sandboxStoppedCount", sandboxStoppedCount);

        return "user/dashboard";
    }

    @GetMapping("/layout")
    public String layout() {
        return "index-temp";
    }


}
