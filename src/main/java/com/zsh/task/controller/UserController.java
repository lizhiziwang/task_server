package com.zsh.task.controller;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("doLogin")
    public SaResult doLogin(String username, String password,String id) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if("zhang".equals(username) && "123456".equals(password)) {
            StpUtil.login(id);
            String tokenValue = StpUtil.getTokenValue();
            System.out.println("当前的token："+tokenValue);

            // 获取所有登录的用户ids
            List<String> logIds = StpUtil.searchSessionId("", 0, -1, false);
            for(String obj : logIds){
                System.out.println(obj);
                System.out.println(StpUtil.getSessionTimeout());
            }



            return SaResult.ok("登录成功");
        }
        return SaResult.error("登录失败");
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
