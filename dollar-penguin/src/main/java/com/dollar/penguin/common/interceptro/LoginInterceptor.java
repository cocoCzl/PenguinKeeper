package com.dollar.penguin.common.interceptro;

import com.auth0.jwt.interfaces.Claim;
import com.dollar.penguin.util.JwtUtil;
import com.dollar.penguin.common.enumUtil.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception{
        try {
            // token 验证
            String token = request.getHeader("authenticate-token");
            if (StringUtils.isNoneBlank(token)) {
                Claim claim = JwtUtil.doParse(token, JwtUtil.KEY);
                if (log.isDebugEnabled()) {
                    log.debug("token:{},claim:{}", token, claim);
                }
                return true;
            }
        } catch (Throwable e) {
            log.error("LoginInterceptor error:{}", e.getMessage(), e);
        }
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(ResultCodeEnum.ERR_LOGIN.code());
        response.getWriter().println("[用户未登录]");
        return false;
    }
}