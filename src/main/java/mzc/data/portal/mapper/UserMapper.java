package mzc.data.portal.mapper;

import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.UserGrade;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    UserData findUser(String loginId);

    void addPasswordErrorCnt(UserData.UpdateMemberParam updateMemberParam);

    void addPasswordLog(@Param("idx") long idx, @Param("loginPw") String loginPw);

    List<UserData.OldPassword> findDuplicatedPw(long idx);

    void addUser(UserData.AddUserParam addUserParam);

    List<UserData> userList();

    UserData getUser(long idx);

    void updateUserPassword(@Param("idx") long idx, @Param("loginPw")String loginPw, @Param("userGrade") UserGrade userGrade);

    void updateUserLastLogin(long idx);

    void resetPasswordErrorCount(long idx);

    void updateUser(UserData.UpdateUserParam updateUserParam);

}
