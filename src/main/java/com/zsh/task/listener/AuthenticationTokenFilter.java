package com.zsh.task.listener;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.zsh.task.cache.UserCache;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.entity.LoginUser;
import com.zsh.task.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    public void destroy() {
        LoginUserThreatContext.clear();
        super.destroy();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        try{
            log.info("当前请求的url:"+request.getRequestURI());

            String token = request.getHeader("Authorization");
            if (StringUtils.isBlank(token)) {
                //放行
//            filterChain.doFilter(request, response);
                JSONObject re = new JSONObject();
                re.put("code",403);
                re.put("data","请携带token访问！请求的接口："+request.getRequestURI());
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
            Object userId =  StpUtil.getLoginIdByToken(token);

            LoginUser lu = uc.get(String.valueOf(userId), LoginUser.class);
            if(Objects.isNull(lu)){
                throw new CacheTimeException("缓存过期！");
            }
            //存放线程变量
            LoginUserThreatContext.setUser(lu.getUser());

            //存入SecurityContextHolder
            //TODO 获取权限信息封装到Authentication中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(lu,null,null);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            //放行
            filterChain.doFilter(request, response);
        }catch (CacheTimeException e){
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
            log.error("用户登录缓存过期：{}",StpUtil.getLoginId());
            return;
        }
        catch (Exception e){
            LoginUserThreatContext.clear();
            throw e;
        }finally {
            LoginUserThreatContext.clear();
        }
    }
}
