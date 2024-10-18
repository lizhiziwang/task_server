package com.zsh.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor poolExecutor(){
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(4,8,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(100));
        tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return tpe;
    }
}
