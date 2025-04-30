package com.zsh.task.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.common.Result;
import com.zsh.task.constant.OrderState;
import com.zsh.task.entity.Order;
import com.zsh.task.entity.OrderGood;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.mapper.OrderGoodMapper;
import com.zsh.task.mapper.OrderMapper;
import com.zsh.task.vo.OrderSearchVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Resource
    TradAccountService tas;
    @Resource
    OrderGoodMapper ogm;


    @Override
    public Order addOrder(Long userId, List<Long> accIds,double sum) {
        Order var = new Order();
        var.setId(IdUtil.getSnowflakeNextId())
                .setCreateTime(new Date())
                .setSum(sum)
                .setState(OrderState.NOPAID.code)
                .setCreateUser(userId)
                .setUpdateTime(new Date());
//                .setCommodityList(JSONArray.parseArray(JSON.toJSONString(accIds)).toJSONString())
        //获取订单账号的信息
        baseMapper.insertSelective(var);
        return var;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Order> addOrder(Order vo) {
        long currentUserId = LoginUserThreatContext.getUser().getId();
        long orderId = IdUtil.getSnowflakeNextId();
        vo.setId(orderId).setCreateTime(new Date()).setState(OrderState.NOPAID.code).setCreateUser(currentUserId)
                .setUpdateTime(new Date());
        List<OrderGood> goods = vo.getGoods();
//        if(goods == null||goods.size() ==0 ){
//            return Result.failed("q")
//        }
        // prent
        goods.forEach(e->{
            e.setId(IdUtil.getSnowflakeNextId());
            e.setCreateUser(currentUserId);
            e.setOrderId(orderId);
        });

        List<Long> goodIds = goods.stream().map(OrderGood::getGoodId).collect(Collectors.toList());

        List<TradAccount> byIds = tas.findByIds(goodIds);
        // 校验库存
        for(TradAccount item :byIds){
            for (OrderGood e:goods){
                if(e.getGoodId() == item.getId()){
                    if(e.getGoodNum() > item.getGameId()){
                        return Result.failed(item.getGameName()+"库存不足");
                    }
                    else {
                        item.setGameId(item.getGameId()-e.getGoodNum());
                    }
                    break;
                }
            }
        }
        double sum = byIds.stream().mapToDouble(TradAccount::getPrice).sum();
        vo.setSum(sum);
        boolean isSuccess = ogm.insert(goods)!=null&&this.save(vo);
        if(!isSuccess){
            throw new RuntimeException("add order failed");
        }
        //更新库存
        tas.saveOrUpdateBatch(byIds);
        vo.setAccounts(byIds);
        return Result.succeed(vo);
    }

    @Override
    public Page<Order> page(OrderSearchVo vo) {
        Page<Order> page = new Page<>(vo.getCurrent() ,vo.getSize());

        QueryWrapper<Order> qw = new QueryWrapper<>();
        if (vo.getCreateUser() !=null) {
            qw.eq("create_user",vo.getCreateUser());
        }
        if (StringUtils.isNoneBlank(vo.getStartTime())&&StringUtils.isNoneBlank(vo.getEndTime())) {
            Date start = DateUtil.parse(vo.getStartTime());
            Date end = DateUtil.parse(vo.getEndTime());

            qw.between("create_time",start,end);
        }
        if (vo.getSumLow()!=null&&vo.getSumTop()!=null) {
            qw.between("sum",vo.getSumLow(),vo.getSumTop());
        }
//        if(vo.getProNum() != null){
//            qw.apply("JSON_LENGTH(commodity_list) = {0}",vo.getProNum());
//        }

        if(vo.getAsc())
            qw.orderByAsc(vo.getOrderBy());
        qw.orderByDesc(vo.getOrderBy());
        //分页查询
        baseMapper.selectPage(page,qw);

        List<Order> records = page.getRecords();

        List<Long> order_id = records.stream().map(Order::getId).collect(Collectors.toList());

        List<Map<String, Object>> byOrder = ogm.findByOrder(order_id);

        Map<Object, List<Map<String, Object>>> orderId = byOrder.stream().collect(Collectors.groupingBy(e -> e.get("orderId")));

        records.forEach(e->{
            e.setState_(OrderState.findByCode(e.getState()).name);
            List<Map<String, Object>> maps = orderId.get(e.getId());

            List<TradAccount> list = JSONObject.parseObject(JSONObject.toJSONString(maps), List.class);
            e.setAccounts(list);

        });
        return page;
    }

    public List<Map<String,Object>> countData(Long pubUser,String pubTime){
        String date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isBlank(pubTime)) {
            LocalDate currentDate = LocalDate.now();
            LocalDate localDate = currentDate.minusMonths(1);
            Date var = new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
            date = sdf.format(var);
        }else {
            Date var = DateUtil.parse(pubTime);
            date = sdf.format(var);
        }
        return baseMapper.countData(pubUser,date);
    }

    public static void main(String[] args) {
        LocalDate currentDate = LocalDate.now();
        LocalDate localDate = currentDate.minusMonths(1);
        Date date = new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        System.out.println(date);
    }
}
