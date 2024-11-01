package com.zsh.task.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.cache.UserCache;
import com.zsh.task.common.Result;
import com.zsh.task.entity.User;
import com.zsh.task.entity.UserIdentity;
import com.zsh.task.service.FriendService;
import com.zsh.task.service.MessageService;
import com.zsh.task.service.UserIdentityService;
import com.zsh.task.service.UserService;
import com.zsh.task.vo.UnreadVo;
import com.zsh.task.vo.UserVo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService us;
    @Resource
    UserCache uc;
    @Resource
    FriendService fs;
    @Resource
    PasswordEncoder encoder;
    @Resource
    MessageService ms;
    @Resource
    UserIdentityService uis;

    @GetMapping("/login")
    public Result<Map<String, Object>> doLogin(@RequestParam(name = "userName")@NotBlank String userName,
                          @RequestParam(name = "password")@NotBlank String password) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
//        User loginUser = us.getByName(userName);
//        if(loginUser == null){
//            return Result.failed("用户不存在！");
//        }
//        String realPwd = loginUser.getPwd();
//        if (realPwd.equals(password)){
//            StpUtil.login(loginUser.getId());
//            String token = StpUtil.getTokenValue();
//            JSONObject jo = new JSONObject();
//            jo.put("user",loginUser);
//            jo.put("token",token);
//            //设置为在线状态
//            loginUser.setIsOnline(1);
//            us.saveOrUpdate(loginUser);
//            //添加用户登录信息缓存
//            uc.put(token,loginUser);
//            return Result.succeed(jo.toJSONString());
//        }else {
//            return Result.failed("登录失败！");
//        }
        Map<String, Object> re = us.doLogin(userName, password);
        return Result.succeed(re);
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
        re.setPwd(encoder.encode(user.getPwd()));
        return Result.succeed(us.save(re));

    }

    @PostMapping("/update")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateUser(@RequestBody User user){
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.ne("id",user.getId())
                .eq("name",user.getName());

        if (us.getOne(qw)!=null) {
            return Result.failed("该用户名已存在！");
        }
        user.setUpdateTime(new Date())
                .setPwd(encoder.encode(user.getPwd()));
        //更改之后需要重新登录
        StpUtil.logoutByLoginId(user.getId());
        //更新角色
        uis.updateByUserId(user.getIdentity(), user.getId());
        return Result.succeed(us.updateById(user));
    }
    @PostMapping("/rem/{id}")
    public Result<Boolean> removeUser(@PathVariable Long id){
        return Result.succeed(us.removeById(id));
    }
    @GetMapping("/down/{id}")
    public Result<Boolean> downLine(@PathVariable Long id){
        return Result.succeed(us.downLine(id));
    }
    //用户列表查询
    @PostMapping("/search")
    public Result<Page<User>> searchUsers(@RequestBody UserVo vo,
                                          @RequestParam(name = "size")Long size,
                                          @RequestParam(name = "current") Long current){

        Page<User> userPage = us.searchUsers(vo, size, current);
        return Result.succeed(userPage);
    }

    // 查询登录状态，浏览器访问： http://localhost:8081/user/isLogin

    /**
     * @param name 查询用户名,传null时会查询当前用户的所有好友
     * @param userId 当前用户ID
     * */
    @GetMapping("/friends/{userId}")
    public Result<List<User>> getAllFriend(@PathVariable Long userId,
                                           @RequestParam(name = "name") String name){
        List<User> allFriend = fs.getAllFriend(userId, name);
        //添加未读数
        List<UnreadVo> vos = ms.selectUnread(userId);
        for(User user:allFriend){
            for (UnreadVo vo:vos){
                if (vo.getSendUser().equals(user.getId())) {
                    user.setMesCount(vo.getCount());
                    break;
                }
            }
        }

        return Result.succeed(allFriend);
    }
    @GetMapping("/toAdmin/{id}")
    public Result<Boolean> toAdmin(@PathVariable Long id){
        QueryWrapper<UserIdentity> qw = new QueryWrapper<>();

        qw.eq("user_id",id);
        UserIdentity identity = uis.getOne(qw);

        if (identity!=null && identity.getIdentity() == 0) {
            return Result.succeed(true);
        }
        return Result.failed("权限不足，请联系管理员！");
    }
}
