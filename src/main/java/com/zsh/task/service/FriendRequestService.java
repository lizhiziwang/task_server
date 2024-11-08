package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.FriendRequest;

import java.util.List;

public interface FriendRequestService extends IService<FriendRequest> {

    boolean addRequest(Long applicant,Long receiver,String mes);

    List<?> getNoAgreeRequest(Long receiver);
}
