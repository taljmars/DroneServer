package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

public class ConnectionInterceptor implements HandlerInterceptor {

    private final static Logger LOGGER = Logger.getLogger(ConnectionInterceptor.class);

    @Scope
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LOGGER.debug("CONNECTION STARTED");
        LOGGER.debug("CONNECTION STARTED request: " + request);
        LOGGER.debug("CONNECTION STARTED response: " + response);
//        Object bean = ((HandlerMethod) handler).getBean();
//        Field[] fields = bean.getClass().getFields();
//        for (Field field : fields) {
//
//        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        LOGGER.debug("CONNECTION ENDED request: " + request);
        LOGGER.debug("CONNECTION ENDED response: " + response);
        LOGGER.debug("CONNECTION ENDED");
    }
}
