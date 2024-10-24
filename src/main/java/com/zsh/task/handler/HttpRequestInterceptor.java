package com.zsh.task.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
public class HttpRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("------------进入拦截器------------");
        String token = request.getHeader("Authorization");
        long tokenTimeout = StpUtil.getTokenTimeout(token);
        if (!(tokenTimeout > 0L)) {
            JSONObject jo = new JSONObject();
            jo.put("code",403);
            jo.put("data","当前请求未被允许，请先登录！");
            jo.put("message","失败");
            // 设置响应状态码
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            // 设置响应内容类型
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter writer = response.getWriter();
            writer.print(jo.toJSONString());
            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }
}
