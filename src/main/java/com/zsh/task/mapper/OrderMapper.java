package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order> {
    int insertSelective(Order order);
    int updateSelective(Order order);
    Page<Order> selectPage_(@Param("page")Page<Order> page,
                            @Param(Constants.WRAPPER)QueryWrapper<Order> qw);
    List<Map<String,Object>> countData(@Param("pubUser") Long pubUser,
                                       @Param("selectTime")String selectTime);
}
