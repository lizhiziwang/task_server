package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.vo.AccountSelectVo;

public interface TradAccountService extends IService<TradAccount> {
    boolean insertSelective(TradAccount entity);
    boolean updateByIdSelective(TradAccount entity);
    boolean addWantNum(Long id,boolean isAdd);

    Page<TradAccount> selectAccountPage(AccountSelectVo params);
}
