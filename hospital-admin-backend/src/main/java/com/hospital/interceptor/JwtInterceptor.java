package com.hospital.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hospital.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    // 完全公开的接口（无需登录）- 注意：Spring MVC 中 URI 不带 /api 前缀
    private static final String[] PUBLIC_PATHS = {
            "/auth/login", "/auth/wx-login", "/auth/wx-register",
            "/department/list", "/doctor/list", "/doctor/detail",
            "/news/list", "/news/detail", "/schedule/list"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        // 去掉 /api 前缀进行匹配（因为上下文路径是 /api）
        String path = uri;
        if (uri.startsWith("/api")) {
            path = uri.substring(4); // 去掉 /api
        }

        // 公开接口免登录
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"请先登录\"}");
            return false;
        }

        String token = authHeader.substring(7);
        try {
            DecodedJWT jwt = JwtUtil.verifyToken(token);
            request.setAttribute("userId", jwt.getClaim("userId").asLong());
            request.setAttribute("username", jwt.getClaim("username").asString());
            request.setAttribute("role", jwt.getClaim("role").asString());
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\"}");
            return false;
        }
    }
}
