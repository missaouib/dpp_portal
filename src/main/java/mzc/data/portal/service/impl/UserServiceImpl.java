package mzc.data.portal.service.impl;


import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.enums.UserGrade;
import mzc.data.portal.enums.UserStatus;
import mzc.data.portal.exception.AlreadyExistException;
import mzc.data.portal.mapper.UserMapper;
import mzc.data.portal.service.UserService;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Slf4j
@Service
@ComponentScan
public class UserServiceImpl extends BaseServiceImpl implements UserDetailsService, UserService {

    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    /**
     * 생성자 주입
     * @param passwordEncoder
     * @param userMapper
     */
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserData getLoginUser() {
        UserData userData = getUser();
        return userData;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) {

        try {
            UserData user = null;
            user = userMapper.findUser(loginId);

            //계정이 아에 없을 경우
            if (user == null) {
                throw new InternalAuthenticationServiceException("아이디 혹은 비밀번호가 틀립니다.");
            }

            if (user.getPwErrCnt() >= 5) {
                throw new LockedException("잠긴 계정입니다. 관리자에게 문의하세요.");
            }

            if (user.getUserStatus() == UserStatus.INACTIVE) {
                throw new DisabledException("비활성화된 계정입니다. 관리자에게 문의하세요.");
            }

            String userGrade = user.getUserGrade().toString();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(userGrade));

            return new User(user.getLoginId(), user.getLoginPw(), authorities);
        } catch (UsernameNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public void addPasswordErrorCnt(String loginId) {
        try {
            UserData.UpdateMemberParam updateMemberParam = UserData.UpdateMemberParam.builder().loginId(loginId).build();
            userMapper.addPasswordErrorCnt(updateMemberParam);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다.관리자에게 문의하세요.");
        }
    }

    @Override
    @Transactional
    public void addUser(UserData.AddUserParam param) {
        Assert.notNull(param, "AddUserParam must be not null");

        String loginId = param.getLoginId();

        try {

            if(duplicated(loginId)) {
                throw new AlreadyExistException("이미 등록된 아이디입니다.");
            }

            String passwd = passwordEncoder.encode(param.getLoginPw());
            String nickName = param.getNickName();

            param.setLoginPw(passwd);
            param.setNickName(nickName);
            param.setUserGrade(UserGrade.ROLE_USER);
            param.setUserStatus(UserStatus.ACTIVE);
            userMapper.addUser(param);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public boolean duplicated(String loginId) {

        UserData findUser = null;

        try {
            findUser = userMapper.findUser(loginId);

            if(findUser == null) {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }
        return true;
    }


    public String defaultPw() {
        String defaultPassword = "d2p1234";
        return defaultPassword;
    }


    public boolean checkDefaultPassword(String loginPw) {
        String defaultPassword = defaultPw();
            if(loginPw.equals(defaultPassword)){
                return true;
            } return false;
    }

    @Override
    public List<UserData> userList() {
        List<UserData> userList = new ArrayList<>();

        try {
            userList = userMapper.userList();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }

        return userList;

    }

    @Override
    public UserData getUser(long idx) {
        UserData userData = null;

        try {
            userData = userMapper.getUser(idx);

            if(userData == null) {
                throw new NotFoundException("잘못된 정보입니다.");
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("시스템 오류입니다. 관리자에게 문의하세요.");
        }

        return userData;
    }

    @Override
    public void updatePassword(UserData.UpdateUserParam param) {
        Assert.notNull(param, "UpdatePasswdParam must be not null");

        try {
            String loginPw = param.getLoginPw();
            String rePassword = param.getRePassword();
            String encodedPassword = passwordEncoder.encode(loginPw);

            param.setEncodedPassword(encodedPassword);

            passwordCheck(param);

            param.setUserGrade(UserGrade.ROLE_USER);

            userMapper.addPasswordLog(param.getIdx(), param.getLoginPw());

            userMapper.updateUserPassword(param.getIdx(), param.getLoginPw(), param.getUserGrade());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void passwordCheck(UserData.UpdateUserParam param) {

        long idx = param.getIdx();
        String loginId = param.getLoginId();
        String loginPw = param.getLoginPw();
        String rePassword = param.getRePassword();
        String encodedPassword = param.getEncodedPassword();

        try {
            if(!StringUtils.equals(loginPw, rePassword)) {
                throw new IllegalArgumentException("입력하신 비밀번호가 다릅니다. 다시 입력해주세요.");
            }

            if(loginPw.contains(loginId)) {
                throw new IllegalArgumentException("비밀번호에 아이디가 포함되어있습니다. 다시 설정해주세요.");
            };

            //초기 비밀번호 중복 검증
            if(checkDefaultPassword(loginPw)) {
                throw new IllegalArgumentException("잘못된 비밀번호입니다.");
            }

            //연속 문자,숫자 4자리가 포함 될 경우
            if(continuousPassword(loginPw)) {
                throw new IllegalArgumentException("연속 문자 숫자 4자리가 있습니다. 다시 설정해주세요.");
            }

            //같은 문자,숫자 4자리 포함 될 경우
            if(samePassword(loginPw)) {
                throw new IllegalArgumentException("동일 문자 숫자 4자리가 있습니다. 다시 설정해주세요.");
            }

            //최근 변경한 5개 비밀번호 중 중복 검증
            if(duplicatedPassword(param)) {
                throw new IllegalArgumentException("이전 비밀번호와 동일합니다. 다시 설정해주세요.");
            }

            // 비밀번호 검증 완료시 암호화된 비밀번호로 저장
            param.setLoginPw(encodedPassword);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }

    }

    public boolean continuousPassword(String loginPw) {

        int pwLength = loginPw.length();

        int[] tempArray = new int[pwLength];

        for (int i = 0; i < pwLength; i++) {
            tempArray[i] = loginPw.charAt(i);
        }

        for (int i = 0; i < pwLength - 3; i++) {
            if ((tempArray[i] > 47 && tempArray[i+3] < 58) || (tempArray[i] > 64 && tempArray[i+3] < 91) || (tempArray[i] > 96 && tempArray[i+3] < 123)) {
                if (Math.abs(tempArray[i+3] - tempArray[i+2]) == 1 && Math.abs(tempArray[i+3] - tempArray[i+1]) == 2 && Math.abs(tempArray[i+3] - tempArray[i]) == 3) {
                    return true;
                }
            }
        } return false;
    }

    public boolean samePassword(String loginPw) {

        String pwPattern = "(\\w)\\1\\1\\1";
        Matcher match = Pattern.compile(pwPattern).matcher(loginPw);

        if (match.find()) {
            return true;
        }
        return false;

    }

    public boolean duplicatedPassword(UserData.UpdateUserParam param) {

        long idx = param.getIdx();
        String loginPw = param.getLoginPw();
        List<UserData.OldPassword> oldPassword = new ArrayList<>();

        try {
            String encodedPassword = passwordEncoder.encode(loginPw);

            oldPassword = userMapper.findDuplicatedPw(idx);

            if (oldPassword.size() == 0) {
                return false;
            }

            for(int i = 0; i < oldPassword.size(); i++) {
                String comparePassword = oldPassword.get(i).getLoginPw();
                if (passwordEncoder.matches(loginPw, comparePassword)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException("시스템 오류입니다. 관리자에게 문의하세요.123");
        }

    }

    @Override
    public void updateLastLogin(long idx) {
        try {
            userMapper.updateUserLastLogin(idx);
        } catch (Exception e) {
            log.error((e.getMessage()));
            throw  new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void resetPasswordErrorCount(long idx) {
        try {
            userMapper.resetPasswordErrorCount(idx);
        } catch (Exception e) {
            log.error((e.getMessage()));
            throw  new IllegalArgumentException(e.getMessage());
        }

    }

    //admin->회원정보 변경해주는 로직
    @Override
    public void updateUserInfo(UserData.UpdateUserParam param) {
        Assert.notNull(param, "UpdatePasswdParam must be not null");

        try {
            userMapper.updateUser(param);

        } catch (Exception e) {
            log.error((e.getMessage()));
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public void initUser(long idx) {

        UserData userData = getUser(idx);
        try {
            String encodedPw = passwordEncoder.encode(defaultPw());
            UserData.UpdateUserParam updateUserParam =
                    UserData.UpdateUserParam.builder().idx(idx).userGrade(UserGrade.ROLE_INIT).loginPw(encodedPw).build();
            updateUserParam.setLoginId(userData.getLoginId());
            updateUserParam.setNickName(userData.getNickName());
            updateUserParam.setUserStatus(userData.getUserStatus());
            userMapper.updateUserPassword(updateUserParam.getIdx(), updateUserParam.getLoginPw(), updateUserParam.getUserGrade());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException(e.getMessage());
        }
    }

}
