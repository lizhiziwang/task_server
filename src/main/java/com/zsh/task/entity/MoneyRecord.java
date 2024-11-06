package com.zsh.task.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "money_record")
public class MoneyRecord {

    private Long id;
    // 操作人
    private Long operator;
    // 充值 TOPUP 提现 WITHDRAW
    private String operatorType;
    private Double operatorAmount;
    private Date createTime;
    private Date updateTime;
    private String alipayId;
    private String result;
}
