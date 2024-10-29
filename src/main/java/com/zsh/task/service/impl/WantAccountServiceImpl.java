package com.zsh.task.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.WantAccount;
import com.zsh.task.mapper.TradAccountMapper;
import com.zsh.task.mapper.WantAccountMapper;
import com.zsh.task.service.WantAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class WantAccountServiceImpl extends ServiceImpl<WantAccountMapper, WantAccount> implements WantAccountService {

    @Resource
    TradAccountMapper tam;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addLike(Long userId, Long accId) {
        QueryWrapper<WantAccount> qw = new QueryWrapper<>();
        qw.eq("user_id",userId).eq("account_id",accId);
        WantAccount one = getOne(qw);
        if (one!=null&&one.getIsWant()==1) {
            one.setIsWant(0)
                    .setUpdateTime(new Date());
            return updateById(one)&&(tam.addOrLeWantNum(accId,false)>0);
        }
        else if(one!=null && one.getIsWant()==0){
            one.setIsWant(1)
                    .setUpdateTime(new Date());
            return updateById(one)&&(tam.addOrLeWantNum(accId,true)>0);
        }

        WantAccount wa = new WantAccount();

        wa.setIsWant(1)
                .setId(IdUtil.getSnowflakeNextId())
                .setAccId(accId)
                .setUserId(userId)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return save(wa)&&(tam.addOrLeWantNum(accId,true)>0);
    }
}
