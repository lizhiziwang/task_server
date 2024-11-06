package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {

    boolean addOrder(Long userId, List<Long> accIds,double sum);
}
