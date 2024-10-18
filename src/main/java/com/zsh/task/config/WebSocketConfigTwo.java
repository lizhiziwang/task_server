package com.zsh.task.config;

import com.zsh.task.handler.MyWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Configuration
@EnableWebSocket
public class WebSocketConfigTwo implements WebSocketConfigurer {

    @Resource
    MyWebSocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/serverTwo")//设置连接路径和处理
                .setAllowedOrigins("*");
//                .addInterceptors(new MyWebSocketInterceptor());//设置拦截器
    }
    /**
     * 自定义拦截器拦截WebSocket请求
     */
    class MyWebSocketInterceptor implements HandshakeInterceptor {
        //前置拦截一般用来注册用户信息，绑定 WebSocketSession
        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                       WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
            System.out.println("前置拦截~~");
            if (!(request instanceof ServletServerHttpRequest)) return true;
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String id = (String) servletRequest.getSession().getAttribute("id");
            attributes.put("id", id);
            return true;
        }
        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Exception exception) {
            System.out.println("后置拦截~~");
        }
    }
}