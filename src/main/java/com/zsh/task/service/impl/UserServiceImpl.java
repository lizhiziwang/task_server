package com.zsh.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.UserMapper;
import com.zsh.task.service.UserService;
import com.zsh.task.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


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
}
