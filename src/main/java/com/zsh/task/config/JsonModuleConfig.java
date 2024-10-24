package com.zsh.task.config;


import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zsh.task.utils.CustomDateSerializer;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JsonModuleConfig extends SimpleModule {
    public JsonModuleConfig() {
        this.addSerializer(Long.class, ToStringSerializer.instance);
        this.addSerializer(Date.class, CustomDateSerializer.instance);
    }
}
