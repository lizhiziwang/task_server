package com.zsh.task.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.cache.UserCache;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.common.Result;
import com.zsh.task.constant.OrderState;
import com.zsh.task.entity.Order;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.entity.User;
import com.zsh.task.service.OrderService;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.service.UserService;
import com.zsh.task.vo.OrderSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService os;
    @Resource
    TradAccountService tas;
    @Resource
    UserService us;
    @Resource
    UserCache uc;
    // 超卖问题
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Order> addOrder(@RequestParam Long [] accIds){
        if (accIds == null||accIds.length == 0){
            return Result.failed("请选择下单对象");
        }
        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        qw.in("id",Arrays.asList(accIds));

        List<TradAccount> ta = tas.list(qw);
        double price = 0;
        for (TradAccount var : ta) {
            if(var.getIsExist() == 0){
                return Result.failed(var.getGameName()+"的账号为："+var.getGameId()+"已被人下单了");
            }
            price += var.getPrice();
            var.setIsExist(0);
        }
        tas.updateExcite(ta);

        Long currentUserId = LoginUserThreatContext.getUser().getId();
        List<Long> ids = Arrays.asList(accIds);
//        double pricesByIds = tas.getPricesByIds(ids);

        return Result.succeed(os.addOrder(currentUserId,ids,price));
    }
    //付款
    @GetMapping("/pay/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Order> pay(@PathVariable Long id){
        User user = LoginUserThreatContext.getUser();

        Order o = os.getById(id);

        double var = user.getPurse()- o.getSum();

        if (var<0) {
            return Result.failed("钱包余额不足,请充值！");
        }
        user.setPurse(var)
                .setUpdateTime(new Date());

        o.setState(OrderState.PAID.code)
                .setUpdateTime(new Date());

        us.updateById(user);
        os.updateById(o);
        uc.updateCache(user.getId());

        return Result.succeed(o);

        //
    }
    // 取消订单
    @GetMapping("/cancel/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> cancelOrder(@PathVariable Long id){
        Order o = os.getById(id);
        if(OrderState.CANCELED.code.equals(o.getState())){
            return Result.failed("该订单已经取消");
        }
        o.setUpdateTime(new Date())
                .setState(OrderState.CANCELED.code);

        os.updateById(o);

        JSONArray ja = JSON.parseArray(o.getCommodityList());
        QueryWrapper<TradAccount> qw = new QueryWrapper<>();

        qw.in("id",ja.toArray());
        List<TradAccount> list = tas.list(qw);
        list.forEach(e->e.setUpdateTime(new Date()).setIsExist(1));
        tas.updateExcite(list);
        return Result.succeed(true);
    }


    @PostMapping("/page")
    public Result<Page<Order>> page(@RequestBody OrderSearchVo vo){
        return Result.succeed(os.page(vo));
    }
}
