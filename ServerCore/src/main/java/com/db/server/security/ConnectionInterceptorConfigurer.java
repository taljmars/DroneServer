package com.db.server.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConnectionInterceptorConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> lst = new ArrayList<>();
        lst.add("/login");
        lst.add("/keepAlive");
        registry.addInterceptor(new ConnectionInterceptor()).addPathPatterns(lst);
    }

}
