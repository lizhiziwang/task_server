package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.common.Result;
import com.zsh.task.entity.Order;
import com.zsh.task.vo.OrderSearchVo;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Order> {

    Order addOrder(Long userId, List<Long> accIds,double sum);
    Result<Order> addOrder(Order vo);

    Page<Order> page(OrderSearchVo vo);
    List<Map<String,Object>> countData(Long pubUser, String pubTime);
}
