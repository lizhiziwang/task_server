package com.zsh.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
//订单商品
@Data
@TableName("ordered_goods")
public class OrderGood implements Serializable {
    @TableId
    private Long id;
    @TableField(value = "good_id")
    private Long goodId;
    @TableField(value = "good_num")
    private Integer goodNum;
    @TableField(value = "order_id")
    private Long orderId;
    @TableField(value = "create_user")
    private Long createUser;


    @TableField(exist = false)
    private Double price;
//    private String unit;
}
