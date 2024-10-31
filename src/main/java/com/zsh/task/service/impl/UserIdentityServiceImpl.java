package com.zsh.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.UserIdentity;
import com.zsh.task.mapper.UserIdentityMapper;
import com.zsh.task.service.UserIdentityService;
import org.springframework.stereotype.Service;

@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityMapper, UserIdentity> implements UserIdentityService {
}
