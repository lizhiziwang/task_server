package com.zsh.task.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zsh.task.common.Result;
import com.zsh.task.entity.User;
import com.zsh.task.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService us;

    @GetMapping("/login")
    public Result<String> doLogin(@RequestParam(name = "userName")@NotBlank String userName,
                          @RequestParam(name = "password")@NotBlank String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        User loginUser = us.getByName(userName);
        if(loginUser == null){
            return Result.failed("用户不存在！");
        }
        String realPwd = loginUser.getPwd();
        if (realPwd.equals(password)){
            StpUtil.login(loginUser.getId());
            JSONObject jo = new JSONObject();
            jo.put("user",loginUser);
            jo.put("token",StpUtil.getTokenValue());
            //设置为在线状态
            loginUser.setIsOnline(1);
            us.saveOrUpdate(loginUser);
            return Result.succeed(jo.toJSONString());
        }else {
            return Result.failed("登录失败！");
        }
    }

    @PostMapping("/register")
    public Result<Boolean> register(@RequestBody User user){
        String userName = user.getName();
        if (StringUtils.isBlank(userName)) {
            return Result.failed("用户名不能为空！");
        }
        User byName = us.getByName(userName);
        if (byName != null) {
            return Result.failed("用户名'"+userName+"'已存在，请重新输入！");
        }
        User re = new User();
        re.setIsOnline(0);
        re.setId(IdUtil.getSnowflakeNextId());
        re.setName(user.getName());
        re.setPwd(user.getPwd());
        return Result.succeed(us.save(re));

    }
    @GetMapping("/down/{id}")
    public Result<Boolean> downLine(@PathVariable Long id){
        return Result.succeed(us.downLine(id));
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }
}
