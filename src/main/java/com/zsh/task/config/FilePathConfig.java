package com.zsh.task.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class FilePathConfig implements WebMvcConfigurer {
    @Value("${game.file.path}")
    String filePath;
    //d:/task_server_game/upload/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        log.error(filePath);
        //和页面有关的静态目录都放在项目的static目录下
        registry.addResourceHandler("/file/get/**").addResourceLocations("file:"+filePath);
    }
}
