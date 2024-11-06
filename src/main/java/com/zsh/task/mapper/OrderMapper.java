package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsh.task.entity.Order;

public interface OrderMapper extends BaseMapper<Order> {
    int insertSelective(Order order);
    int updateSelective(Order order);
}
