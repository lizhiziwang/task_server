package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.Order;
import com.zsh.task.vo.OrderSearchVo;

import java.util.List;

public interface OrderService extends IService<Order> {

    Order addOrder(Long userId, List<Long> accIds,double sum);

    Page<Order> page(OrderSearchVo vo);
}
