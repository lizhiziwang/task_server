package com.zsh.task.config;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonModuleConfig extends SimpleModule {
    public JsonModuleConfig() {
        //super(JsonModuleConfig.class.getName());
        this.addSerializer(Long.class, ToStringSerializer.instance);
        this.addSerializer(Long.TYPE, ToStringSerializer.instance);
    }
}
