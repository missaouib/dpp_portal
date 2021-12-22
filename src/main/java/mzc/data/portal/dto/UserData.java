package mzc.data.portal.dto;

import lombok.*;
import mzc.data.portal.enums.UserGrade;
import mzc.data.portal.enums.UserStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Getter
public class UserData {

    private long idx;
    private String loginId;
    private String loginPw;
    private String nickName;
    private UserStatus userStatus;
    private UserGrade userGrade;
    private long pwErrCnt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateMemberParam {
        private String loginId;
        private UserStatus userStatus;
        private UserGrade userGrade;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddUserParam {
        @NotNull(message = "아이디를 입력해주세요.")
        @NotBlank(message = "아이디를 입력해주세요.")
        //@Size(min = 1, max = 64, message = "비밀번호는 최소 {min} , 최대 {max} 자리까지 입력 가능합니다.")
        //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$", message = "특수문자, 영문, 숫자 조합과 8-20자리인지 확인하여주세요.")
        private String loginId;

        @NotNull(message = "비밀번호를 입력해주세요.")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String loginPw;

        //@NotNull(message = "닉네임을 입력해주세요.")
        //@NotBlank(message = "닉네임을 입력해주세요.")
        private String nickName;

        private UserGrade userGrade;
        private UserStatus userStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateUserParam {
        private long idx;
        private String loginId;
        private String loginPw;
        private String rePassword;
        private String encodedPassword;
        private String nickName;
        private UserGrade userGrade;
        private UserStatus userStatus;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OldPassword {
        private String loginPw;
    }

    @NoArgsConstructor
    public static class UserDataCustomRes<T> extends ApiResponse<T> {
        public UserDataCustomRes(int status, String error, T data) {
            super(status, error, data);
        }
    }
}
