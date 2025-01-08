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
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(6,12,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(200));
        tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return tpe;
    }
}
