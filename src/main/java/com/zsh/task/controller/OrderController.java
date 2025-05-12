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
import com.zsh.task.entity.OrderGood;
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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    //todo 超卖问题
    @PostMapping
    public Result<Order> addOrder(@RequestBody Order vo){
        return os.addOrder(vo);

//        if (accIds == null||accIds.length == 0){
//            return Result.failed("请选择下单对象");
//        }
//        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
//        qw.in("id",Arrays.asList(accIds));
//
//        List<TradAccount> ta = tas.list(qw);
//        double price = 0;
//        for (TradAccount var : ta) {
//            if(var.getIsExist() == 0){
//                return Result.failed(var.getGameName()+"的账号为："+var.getGameId()+"已被人下单了");
//            }
//            price += var.getPrice();
//            var.setIsExist(0);
//        }
//        tas.updateExcite(ta);
//
//        Long currentUserId = LoginUserThreatContext.getUser().getId();
//        List<Long> ids = Arrays.asList(accIds);
////        double pricesByIds = tas.getPricesByIds(ids);
//
//        return Result.succeed(os.addOrder(currentUserId,ids,price));
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

        List<TradAccount> orderGoods = os.findOrderGoods(o.getId());
        List<OrderGood> goodsByOrderId = os.getGoodsByOrderId(o.getId());

        Map<Long, OrderGood> collect = goodsByOrderId.stream().collect(Collectors.toMap(OrderGood::getGoodId, e -> e));

        for (TradAccount orderGood : orderGoods) {
            orderGood.setGameId(orderGood.getGameId()-collect.get(orderGood.getId()).getGoodNum());
        }

        tas.updateBatchById(orderGoods);
        return Result.succeed(true);
    }


    @PostMapping("/page")
    public Result<Page<Order>> page(@RequestBody OrderSearchVo vo){
        return Result.succeed(os.page(vo));
    }

    @PostMapping(value = "/refund/{id}")
    public Result<Boolean> refund(@PathVariable Long id){
        Order order = os.getById(id);

        order.setUpdateTime(new Date())
                .setState(OrderState.REFUNDED.code);
        os.updateById(order);
//        JSONArray ja = JSON.parseArray(order.getCommodityList());
        QueryWrapper<TradAccount> qw = new QueryWrapper<>();

        qw.in("id",id);
        List<TradAccount> list = tas.list(qw);
        list.forEach(e->e.setUpdateTime(new Date()).setIsExist(1));
        tas.updateExcite(list);
        // 余额
        us.purseUpOrDown(-order.getSum(),LoginUserThreatContext.getUser().getId());
        return Result.succeed(true);
    }
    @PostMapping(value = "/singfor/{id}")
    public Result<Boolean> singfor(@PathVariable Long id){
        Order byId = os.getById(id);
        byId.setUpdateTime(new Date())
                .setState(OrderState.COMPLETE.code);
        return Result.succeed(os.updateById(byId));
    }
    @GetMapping("/countData")
    public Result<List<Map<String,Object>>> countData(@RequestParam(name = "pubUser")Long pubUser,
                                                      @RequestParam(name = "date")String date){
        List<Map<String, Object>> maps = os.countData(pubUser, date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        maps.forEach(e-> e.put("time",sdf.format(e.get("time"))));
        return Result.succeed(maps);
    }

    @GetMapping("/fahuo/{id}")
    public Result<Boolean> fahuo(@PathVariable Long id){

        Order order = os.getById(id);

        order.setUpdateTime(new Date())
                .setState(OrderState.DELIVERED.code);
        return Result.succeed(os.updateById(order));
    }
    @GetMapping("/complete/{id}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> complete(@PathVariable Long id){
        Order byId = os.getById(id);

        byId.setUpdateTime(new Date())
                .setState(OrderState.COMPLETE.code);

        boolean b = us.purseUpOrDown(byId.getSum(), LoginUserThreatContext.getUser().getId());
        if(b&&os.updateById(byId)){
            return Result.succeed(true);
        }
        else {
            throw new RuntimeException("new Exception");
        }
    }
}
