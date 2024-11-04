package com.zsh.task.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.FriendRequest;
import com.zsh.task.mapper.FriendRequestMapper;
import com.zsh.task.service.FriendRequestService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper,FriendRequest> implements FriendRequestService {
    @Override
    public boolean addRequest(Long applicant, Long receiver) {
        FriendRequest fr = new FriendRequest();
        fr.setId(IdUtil.getSnowflakeNextId())
                .setApplicant(applicant)
                .setReceiver(receiver)
                .setIsAgree(0)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return baseMapper.insert(fr)>0;
    }
}
