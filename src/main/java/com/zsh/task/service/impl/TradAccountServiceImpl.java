package com.zsh.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.constant.InventoryUnit;
import com.zsh.task.constant.OrderState;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.mapper.TradAccountMapper;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.utils.TimeUtils;
import com.zsh.task.vo.AccountSelectVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
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
    @Transactional(rollbackFor = Exception.class)
    public boolean addWantNum(Long id,boolean isAdd) {
        return baseMapper.addOrLeWantNum(id,isAdd)>0;
    }

    @Override
    public Page<TradAccount> selectAccountPage(AccountSelectVo params) {
        Page<Map> page = new Page<>(params.getCurrent(),params.getSize());

        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        qw.eq("is_exist",1);

        if((!"ALL".equals(params.getGameType())) && (!"MY".equals(params.getGameType()))
                && StringUtils.isNoneBlank(params.getGameType())){
            qw.eq("game_type",params.getGameType());
        }else if("MY".equals(params.getGameType())){
            //todo 待实现
            qw.eq("t1.pub_user",LoginUserThreatContext.getUser().getId());
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
        boolean b = StringUtils.isNoneBlank(params.getOrderBy());
        if(!b){
            if (params.isDesc()){
                qw.orderByDesc("want_num");
            }else {
                qw.orderByAsc("want_num");
            }
        }else {
            if (params.isDesc()){
                qw.orderByDesc(params.getOrderBy());
            }else {
                qw.orderByAsc(params.getOrderBy());
            }
        }
        Page<TradAccount> tradAccountPage = baseMapper.selectPage(LoginUserThreatContext.getUser().getId(), page, qw);
        tradAccountPage.getRecords().forEach(e-> e.setUnit(InventoryUnit.findByCode(e.getUnit()).getAlia()));


        return tradAccountPage;
    }
    @Override
    public Page<TradAccount> selectPage(Page<TradAccount> page,QueryWrapper<TradAccount> qw){
        return baseMapper.selectPage(LoginUserThreatContext.getUser().getId(), page,qw);
    }

    @Override
    public double getPricesByIds(List<Long> ids) {
        return baseMapper.getPricesByIds(ids);
    }

    @Override
    public void updateExcite(List<TradAccount> items) {
        baseMapper.updateExcite(items);
    }

    public List<Map<String,Object>> typeCount(Long pubUser,String pubTime){
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(pubTime)) {
            date = TimeUtils.last30dayStr();
        }else {
            Date var = DateUtil.parse(pubTime);
            date = sdf.format(var);

        }

        return baseMapper.typeCount(pubUser,date);
    }

    public Page<?> selectPage_2(Map<String,Object> param){

        Long pubUser =  Long.valueOf(param.get("pubUser").toString());
        long current = Long.parseLong(param.get("current").toString());
        long size = Long.parseLong(param.get("size").toString());

        QueryWrapper<?> qw = new QueryWrapper<>();
        qw.orderByDesc("t1.want_num");

        Page<Map<String,Object>> page = new Page<>(current, size);

        baseMapper.selectPage_2(pubUser,page,qw);
        List<Map<String,Object>> records = page.getRecords();

        records.forEach(e->{
            Object state = e.get("state");
            if (state == null) {
                e.put("state_","未售出");
            }else {
                e.put("state_", OrderState.findByCode(state.toString()).name);
            }
        });

        return page;
    }

    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        LocalDate localDate = currentDate.minusMonths(1);
        Date from = new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());

        System.out.println(from);
    }
}
