package mzc.data.portal.controller;


import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.ApiResponse;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.UserGrade;
import mzc.data.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    private UserService userService;
    private UserGrade userGrade;

    @Autowired
    public AdminUserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("list")
    public String userList(Model model) {

        List<UserData> userList = userService.userList();

        //log.error(userList);
        model.addAttribute("userList", userList);
        return "admin/user-list";
    }

    @Deprecated
    @GetMapping("add")
    public String userAddForm(Model model) {
        return "admin/user-add";
    }

    @PostMapping("add")
    @ResponseBody
    public UserData.UserDataCustomRes userAdd(@Valid UserData.AddUserParam param, Model model) {

        userService.addUser(param);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

    @GetMapping("{idx}")
    public String userInfo(@PathVariable long idx, Model model) {

        UserData userData = userService.getUser(idx);

        model.addAttribute("userData", userData);
        return "admin/user-info";
    }

    @PostMapping("{idx}/info")
    @ResponseBody
    public UserData.UserDataCustomRes userInfoUpdate(@PathVariable long idx, @Valid UserData.UpdateUserParam param, Model model) {
        param.setIdx(idx);
        userService.updateUserInfo(param);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

    //회원 상세에서 사용자 초기화
    @PostMapping("{idx}/init")
    @ResponseBody
    public UserData.UserDataCustomRes userInfoInit(@PathVariable long idx, Model model) {

        userService.initUser(idx);
        return new UserData.UserDataCustomRes<>(ApiResponse.OK, null, null);
    }

}
