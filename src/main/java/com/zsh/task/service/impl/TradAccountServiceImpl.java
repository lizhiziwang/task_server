package com.zsh.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.mapper.TradAccountMapper;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.vo.AccountSelectVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class TradAccountServiceImpl extends ServiceImpl<TradAccountMapper, TradAccount> implements TradAccountService {
    @Override
    public boolean insertSelective(TradAccount entity) {
        return baseMapper.insertSelective(entity)>0;
    }

    @Override
    public boolean updateByIdSelective(TradAccount entity) {
        return baseMapper.updateByIdSelective(entity)>0;
    }

    @Override
    public boolean addWantNum(Long id,boolean isAdd) {
        return baseMapper.addOrLeWantNum(id,isAdd)>0;
    }

    @Override
    public Page<TradAccount> selectAccountPage(AccountSelectVo params) {
        Page<Map> page = new Page<>(params.getCurrent(),params.getSize());

        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        if((!"ALL".equals(params.getGameType()))&&(!"MY".equals(params.getGameType()))){
            qw.eq("game_type",params.getGameType());
        }else if("MY".equals(params.getGameType())){
            //todo 待实现
            qw.eq("t2.user_id",LoginUserThreatContext.getUser().getId());
        }

        if(params.getPubUser() != null){
            qw.eq("pub_user",params.getPubUser());
        }
        if(params.getWantNum() != null){
            qw.lt("want_num",params.getWantNum());
        }
        if (params.getLowPrice()!=null && params.getUpPrice()!=null){
            qw.between("price",params.getLowPrice(),params.getUpPrice());
        }
        if(StringUtils.isNoneBlank(params.getStartTime())&&StringUtils.isNoneBlank(params.getEndTime())){
            Date start = DateUtil.parse(params.getStartTime());
            Date end = DateUtil.parse(params.getEndTime());
            qw.between("create_time",start,end);
        }
        if(StringUtils.isNoneBlank(params.getDesText()))
            qw.like("des_text",params.getDesText());
        if (params.isDesc()){
            qw.orderByDesc(params.getOrderBy());
        }else {
            qw.orderByAsc(params.getOrderBy());
        }
        return baseMapper.selectPage(LoginUserThreatContext.getUser().getId(), page,qw);
    }
}
