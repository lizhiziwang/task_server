package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.FriendRequest;

public interface FriendRequestService extends IService<FriendRequest> {

    boolean addRequest(Long applicant,Long receiver);
}
