package com.zsh.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsh.task.entity.LoginUser;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    PasswordEncoder encoder;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",username);
        User user = userMapper.selectOne(wrapper);
        log.error("加密密码："+user.getPwd());
        log.error("密码验证结果："+encoder.matches("123456",user.getPwd()));

        BigDecimal bd = BigDecimal.valueOf(user.getPurse()).setScale(2, RoundingMode.HALF_UP);
        user.setPurse(bd.doubleValue());

        //如果查询不到数据就通过抛出异常来给出提示
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //TODO 根据用户查询权限信息 添加到LoginUser中

        //封装成UserDetails对象返回
        return new LoginUser(user);
    }

}
