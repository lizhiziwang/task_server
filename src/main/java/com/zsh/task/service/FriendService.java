package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.Friend;
import com.zsh.task.entity.User;

import java.util.List;

public interface FriendService extends IService<Friend> {
    List<User> getAllFriend(Long userId,String name);
}
