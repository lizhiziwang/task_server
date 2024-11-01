package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.UserIdentity;

public interface UserIdentityService extends IService<UserIdentity> {
    boolean updateByUserId(int identity,Long userId);
}
