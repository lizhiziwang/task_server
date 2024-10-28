package com.zsh.task.listener;

import com.alibaba.fastjson.JSONObject;
import com.zsh.task.cache.UserCache;
import com.zsh.task.entity.LoginUser;
import com.zsh.task.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    UserCache uc;
    @Value("${game.url.notVerify}")
    String notVerify;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        String[] par = notVerify.split(",");
        for(String var: par){
            if(requestURI.contains(var)){
                return true;
            }
        }
        return super.shouldNotFilter(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            //放行
//            filterChain.doFilter(request, response);
            JSONObject re = new JSONObject();
            re.put("code",403);
            re.put("data","请携带token访问！");
            re.put("message","失败");
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.print(re);
            writer.flush();
            writer.close();
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法！");
        }
        LoginUser lu = uc.get(userid, LoginUser.class);
        if(Objects.isNull(lu)){
            throw new RuntimeException("缓存过期！");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(lu,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }
}
