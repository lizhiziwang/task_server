package com.zsh.task.config;

import com.zsh.task.handler.HttpRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class TokenCheck implements WebMvcConfigurer {

    @Resource
    HttpRequestInterceptor hri;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(hri)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }
}
