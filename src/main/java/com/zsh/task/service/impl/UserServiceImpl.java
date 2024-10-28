package com.zsh.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.cache.UserCache;
import com.zsh.task.entity.LoginUser;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.UserMapper;
import com.zsh.task.service.UserService;
import com.zsh.task.utils.JwtUtil;
import com.zsh.task.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    UserCache uc;
    @Override
    public Map<String,Object> doLogin(String name, String pwd) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(name,pwd);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        uc.put(userId,loginUser);
        Map<String ,Object> re = new HashMap<>();

        re.put("user",loginUser.getUser());
        re.put("token",jwt);
        return re;
    }

    @Override
    public User getByName(String name) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name",name);
        return getOne(wrapper);
    }

    @Override
    public Boolean downLine(Long userId) {
        User user = this.getById(userId);
        return user != null && this.updateById(user);
    }

    @Override
    public Page<User> searchUsers(UserVo vo, Long size, Long current) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        if (StringUtils.isNoneBlank(vo.getName())) {
            wrapper.like("name",vo.getName());
        }
        if (StringUtils.isNoneBlank(vo.getAlia())) {
            wrapper.like("alia",vo.getAlia());
        }
        if (vo.getIsOnline() != null) {
            wrapper.eq("is_online",vo.getIsOnline());
        }
        if (StringUtils.isNoneBlank(vo.getPhone())) {
            wrapper.like("phone",vo.getPhone());
        }
        if (vo.getGender() != null) {
            wrapper.eq("gender",vo.getGender());
        }
        if (StringUtils.isNoneBlank(vo.getBirthdayStart())&&StringUtils.isNoneBlank(vo.getBirthdayEnd())) {
            String var1 = vo.getBirthdayStart();
            String var2 = vo.getBirthdayEnd();
            Date start = DateUtil.parse(var1);
            Date end = DateUtil.parse(var2);
            wrapper.between("birthday",start,end);
        }

        return page(new Page<>(current,size),wrapper);
    }

    @Override
    public List<User> findFriends(Long userId, String userName) {
        return baseMapper.findFriends(userId,userName);
    }

    @Override
    public Map<Long, User> getTwoUserToMap(Long id1, Long id2) {
        return baseMapper.getTwoUserToMap(id1,id2);
    }
}
