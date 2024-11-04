package com.zsh.task.service;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.Order;
import com.zsh.task.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Override
    public boolean addOrder(Long userId, List<Long> accIds,double sum) {
        Order var = new Order();
        var.setId(IdUtil.getSnowflakeNextId())
                .setCreateTime(new Date())
                .setSum(sum)
                .setCreateUser(userId)
                .setUpdateTime(new Date())
                .setCommodityList(JSONArray.parseArray(JSON.toJSONString(accIds)).toJSONString());
        //获取订单账号的信息
        return baseMapper.insertSelective(var)>0;
    }
}
