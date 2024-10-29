package com.zsh.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "want_account")
public class WantAccount implements Serializable {
    @TableId
    private Long id;
    @TableField(value = "account_id",jdbcType = JdbcType.BIGINT)
    private Long accId;
    @TableField(value = "user_id",jdbcType = JdbcType.BIGINT)
    private Long userId;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "is_want",jdbcType = JdbcType.SMALLINT)
    private Integer isWant;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
}
