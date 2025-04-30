package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.WantAccount;

import java.util.List;

public interface WantAccountService extends IService<WantAccount> {
    /**
     * @param userId 用户id
     * @param accId 想要的账户ID
     * @description 添加或取消想要
     * */
    boolean addLike(Long userId,Long accId);

    boolean addOrCancelLike(long userId, List<Long> accId);

}
