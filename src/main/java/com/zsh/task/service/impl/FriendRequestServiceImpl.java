package com.zsh.task.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.FriendRequest;
import com.zsh.task.mapper.FriendRequestMapper;
import com.zsh.task.mapper.UserMapper;
import com.zsh.task.service.FriendRequestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper,FriendRequest> implements FriendRequestService {
    @Resource
    UserMapper um;
    @Override
    public boolean addRequest(Long applicant, Long receiver,String mes) {
        FriendRequest fr = new FriendRequest();
        fr.setId(IdUtil.getSnowflakeNextId())
                .setApplicant(applicant)
                .setReceiver(receiver)
                .setIsAgree(-1)
                .setReason(mes)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return baseMapper.insert(fr)>0;
    }

    @Override
    public List<?> getNoAgreeRequest(Long receiver) {

        return um.getRequestNoAgree(receiver);
    }
}
