package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<Long> getFriendsId(Long userId);
}
