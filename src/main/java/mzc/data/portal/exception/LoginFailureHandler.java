package mzc.data.portal.exception;

import mzc.data.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof AuthenticationServiceException) {
            request.setAttribute("loginFailMsg", "아이디 혹은 비밀번호가 틀립니다.");
        }

        if (exception instanceof InternalAuthenticationServiceException) {
            request.setAttribute("loginFailMsg", exception.getMessage());
        }

        if (exception instanceof BadCredentialsException) {
            String loginId = request.getParameter("username");
            addFailCnt(loginId);
            request.setAttribute("loginFailMsg", "아이디 혹은 비밀번호가 틀립니다.");
        }

        if (exception instanceof LockedException) {
            request.setAttribute("loginFailMsg", "잠긴 계정입니다. 관리자에게 문의하세요.");
        }

        if (exception instanceof DisabledException) {
            request.setAttribute("loginFailMsg", "비활성화된 계정입니다. 관리자에게 문의하세요.");
        }

        if (exception instanceof AccountExpiredException) {
            request.setAttribute("loginFailMsg", "만료된 계정입니다..");
        }

        if (exception instanceof CredentialsExpiredException) {
            request.setAttribute("loginFailMsg", "비밀번호가 만료되었습니다.");
        }

        request.getRequestDispatcher("/loginPage").forward(request, response);
    }

    public void addFailCnt(String loginId) {
        try {
            userService.addPasswordErrorCnt(loginId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}