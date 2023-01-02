package com.unigem.server.interceptors;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class AccessRestrictionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            if (request.getHeader("API-KEY") != null && request.getHeader("API-KEY").equals("ABCD")) {
                return true;
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return HandlerInterceptor.super.preHandle(request, response, handler);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
