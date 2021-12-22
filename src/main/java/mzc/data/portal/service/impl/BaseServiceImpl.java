package mzc.data.portal.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import mzc.data.portal.dto.AgentLogData;
import mzc.data.portal.dto.UserData;
import mzc.data.portal.mapper.AgentLogMapper;
import mzc.data.portal.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class BaseServiceImpl {

    @Autowired
    protected ModelMapper modelMapper;

    @Autowired
    protected AgentLogMapper agentLogMapper;

    @Autowired
    protected UserMapper userMapper;

    protected <T, E> T convert(E e, Class<T> tClass) {
        return this.modelMapper.map(e, tClass);
    }

    /**
     * 현재 로그인 되어있는 나의 계정 정보를 조회, 이후 상황에 맞게 검증 로직이용
     * @return
     */
    protected UserData getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginId = user.getUsername();

        try {
            UserData userData = userMapper.findUser(loginId);
            return userData;
        } catch (Exception e) {
            throw new IllegalArgumentException("User does not exist");
        }
    }

    /**
     * 특정 유저 정보를 조회, 이후 상황에 맞게 검증 로직이용
     * @return
     */
    protected UserData getUser(String loginId) {
        try {
            UserData userData = userMapper.findUser(loginId);
            return userData;
        } catch (Exception e) {
            throw new IllegalArgumentException("User does not exist");
        }
    }


    protected <T extends Object> String getObjectToJson(T param) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(param);

        return jsonString;
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        if (StringUtils.isNotBlank(clientIp)) {
            List<String> clientIps = Lists.newArrayList(Splitter.on(",").split(clientIp));
            clientIp = clientIps.stream().findFirst().orElse(null);
        }
        return clientIp;
    }

    private String getHeaders(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            builder.append(headerName).append("=").append(request.getHeader(headerName)).append(",");
        }
        return builder.toString();
    }

    protected <T extends Object> void addAgentLog(T object) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        try {
            String txId = UUID.randomUUID().toString();
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String clientId = user.getUsername();
            String clientIp = getClientIp(request);
            String uri = request.getRequestURI();
            String headers = getHeaders(request);
            String method = request.getMethod();
            String params = getObjectToJson(object);

            AgentLogData.AddAgentLogParam agentLog = AgentLogData.AddAgentLogParam.builder()
                    .txId(txId)
                    .uri(uri)
                    .method(method)
                    .clientId(clientId)
                    .clientIp(clientIp)
                    .headers(headers)
                    .params(params)
                    .build();

            agentLogMapper.add(agentLog);

        } catch (Exception e) {
            log.info("Request 정보 로그기록 에러: " + e.getMessage(), e);
        }

    }



}
