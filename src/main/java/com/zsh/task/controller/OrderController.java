package com.zsh.task.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.common.Result;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.service.OrderService;
import com.zsh.task.service.TradAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService os;
    @Resource
    TradAccountService tas;
    // 超卖问题
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addOrder(@RequestParam Long [] accIds){
        if (accIds == null||accIds.length == 0){
            return Result.failed("请选择下单对象");
        }
        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        qw.in("id",Arrays.asList(accIds));

        List<TradAccount> ta = tas.list(qw);
        double price = 0;
        for (TradAccount var : ta) {
            if(var.getIsExist() == 0){
                return Result.succeed(false,var.getGameName()+"的账号为："+var.getGameId()+"已被人购买了");
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

}
