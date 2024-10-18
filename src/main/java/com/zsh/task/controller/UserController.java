package com.zsh.task.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsh.task.common.Result;
import com.zsh.task.entity.User;
import com.zsh.task.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService us;

    @GetMapping("doLogin")
    public Result<String> doLogin(@RequestParam(name = "userName")@NotBlank String userName,
                          @RequestParam(name = "password")@NotBlank String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        User loginUser = us.getByName(userName);
        String realPwd = loginUser.getPwd();
        if (realPwd.equals(password)){
            StpUtil.login(loginUser.getId());
        }else {
            return Result.failed("登录失败！");
        }
        JSONObject jo = new JSONObject();
        jo.put("user",loginUser);
        jo.put("token",StpUtil.getTokenValue());
        return Result.succeed(jo.toJSONString());
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
