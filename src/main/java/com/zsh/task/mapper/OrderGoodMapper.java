package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsh.task.entity.OrderGood;
import com.zsh.task.entity.TradAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderGoodMapper extends BaseMapper<OrderGood> {
    /**
     * 查询订单商品详情
     * */
    List<Map<String,Object>> findByOrder(@Param("ids")List<Long> ids);

}
