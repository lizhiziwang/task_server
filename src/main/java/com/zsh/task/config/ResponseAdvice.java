//package com.zsh.task.config;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@ControllerAdvice
//public class ResponseAdvice implements ResponseBodyAdvice {
//    /**
//     * 内容是否需要重写（通过此方法可以选择性部分控制器和方法进行重写）
//     * 返回 true 表示重写
//     */
//    @Override
//    public boolean supports(MethodParameter returnType, Class converterType) {
//        return true;
//    }
//    /**
//     * 方法返回之前调用此方法
//     */
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
//                                  Class selectedConverterType, ServerHttpRequest request,
//                                  ServerHttpResponse response) {
//        // 设置跨域
//        response.getHeaders().set("Access-Control-Allow-Origin", "*");
//        return body;
//    }
//}