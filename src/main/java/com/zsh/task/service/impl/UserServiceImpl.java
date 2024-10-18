package com.zsh.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.UserMapper;
import com.zsh.task.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public List<Long> getFriendsId(Long userId) {
        return null;
    }
}
