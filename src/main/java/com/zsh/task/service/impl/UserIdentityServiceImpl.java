package com.zsh.task.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.UserIdentity;
import com.zsh.task.mapper.UserIdentityMapper;
import com.zsh.task.service.UserIdentityService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserIdentityServiceImpl extends ServiceImpl<UserIdentityMapper, UserIdentity> implements UserIdentityService {
    @Override
    public boolean updateByUserId(int identity, Long userId) {
        QueryWrapper<UserIdentity> qw = new QueryWrapper<>();
        qw.eq("user_id",userId);

        UserIdentity one = getOne(qw);
        if (one == null) {
            UserIdentity var = new UserIdentity();
            var.setId(IdUtil.getSnowflakeNextId())
                    .setIdentity(identity)
                    .setUserId(userId)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date());

            return save(var);
        }else {
            one.setIdentity(identity)
                    .setUpdateTime(new Date());
            return updateById(one);
        }
    }
}
