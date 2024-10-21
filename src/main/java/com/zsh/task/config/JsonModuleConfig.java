//package com.zsh.task.config;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializeConfig;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.alibaba.fastjson.serializer.ToStringSerializer;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class JsonModuleConfig {
//
//    @Bean
//    public SerializeConfig serializeConfig() {
//        SerializeConfig serializeConfig = new SerializeConfig();
//        serializeConfig.put(Long.class, ToStringSerializer.instance);
//        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
//        return serializeConfig;
//    }
//
////    @Bean
////    public ObjectMapper fastjsonObjectMapper(SerializeConfig serializeConfig) {
////        ObjectMapper objectMapper = new ObjectMapper(serializeConfig);
////        return objectMapper;
////    }
//}
