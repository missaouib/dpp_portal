package mzc.data.portal.controller;

import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class UserController {

    private UserService userService;

    /**
     * 생성자 주입
     * @param userService
     */
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/loginPage")
    public String loginForm(Model model) {
        return "login";
    }

    @GetMapping("/sign-up")
    public String signUp(Model model) {

        return "signup";
    }
//signup
    @PostMapping("/sign-up2")
    @ResponseBody
    public UserData.UserDataCustomRes userAdd(@Valid UserData.AddUserParam param, Model model) {

        userService.addUser(param);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @RequestMapping("/login-success")
    public String loginSuccessRedirect(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
        UserData userData = userService.getLoginUser();

        userService.updateLastLogin(userData.getIdx());
        userService.resetPasswordErrorCount(userData.getIdx());

        String role = auth.getAuthorities().toString();

        if(role.contains("ROLE_INIT")) {
            return "redirect:/check-passwd";
        }
        if(role.contains("ROLE_ADMIN")) {
            return "redirect:/admin";
        }
        return "redirect:/main";
    }

    @GetMapping("/user-info")
    public String userInfoForm(Model model) {
        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        model.addAttribute("userData", userData);
        return "user/user-info";
    }

    @PutMapping("/user-info")
    @ResponseBody
    public UserData.UserDataCustomRes userInfoUpdate(@Valid UserData.UpdateUserParam param, Model model) {

        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setIdx(userData.getIdx());
        param.setUserStatus(userData.getUserStatus());
        param.setUserGrade(userData.getUserGrade());
        param.setLoginId(userData.getLoginId());
        userService.updatePassword(param);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @Deprecated
    @GetMapping("/new/login")
    public String newLogin(Model model) {

        return "login-new";
    }

    @GetMapping("/check-passwd")
    public String checkPwForm(Model model) {
        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        model.addAttribute("userData", userData);
        return "user/check-passwd";
    }

    @PostMapping("/check-passwd")
    @ResponseBody
    public UserData.UserDataCustomRes checkPwUpdate(@Valid UserData.UpdateUserParam param, Model model) {
        UserData loginData = userService.getLoginUser();
        UserData userData = userService.getUser(loginData.getIdx());
        param.setIdx(userData.getIdx());
        param.setLoginId(userData.getLoginId());
        userService.updatePassword(param);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

}
