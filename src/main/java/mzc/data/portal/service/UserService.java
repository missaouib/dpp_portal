package mzc.data.portal.service;

import mzc.data.portal.dto.UserData;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {

    UserData getLoginUser();

    void addPasswordErrorCnt(String loginId);

    void addUser(UserData.AddUserParam param);

    List<UserData> userList();

    UserData getUser(long idx);

    void updatePassword(UserData.UpdateUserParam param);

    void updateLastLogin(long idx);

    void resetPasswordErrorCount(long idx);

    void updateUserInfo(UserData.UpdateUserParam param);

    void initUser(long idx);

}
