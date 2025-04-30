package com.zsh.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName(value = "orders")
public class Order implements Serializable {

    @TableId
    private Long id;
//    @TableField(value = "commodity_list",jdbcType = JdbcType.VARCHAR)
//    private String commodityList;
    @TableField(value = "state",jdbcType = JdbcType.VARCHAR)
    private String state;
    @TableField(value = "create_user",jdbcType = JdbcType.BIGINT)
    private Long createUser;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
    @TableField(value = "sum",jdbcType = JdbcType.DOUBLE)
    private Double sum;
    @TableField(value = "delivery_address")
    private String deliveryAddress;
    @TableField(value = "delivery_user")
    private String deliveryUser;
    @TableField(value = "delivery_phone")
    private String deliveryPhone;


    @TableField(exist = false)
    private List<OrderGood> goods;

    @TableField(exist = false)
    private List<TradAccount> accounts;
    @TableField(exist = false)
    private String state_;
    @TableField(exist = false)
    private String showImg;

}
