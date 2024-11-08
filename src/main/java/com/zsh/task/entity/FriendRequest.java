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
@TableName(value = "friend_request")
public class FriendRequest implements Serializable {

    @TableId
    private Long Id;
    @TableField(value = "applicant",jdbcType = JdbcType.BIGINT)
    private Long applicant;
    @TableField(value = "receiver",jdbcType = JdbcType.BIGINT)
    private Long receiver;
    @TableField(value = "is_agree",jdbcType = JdbcType.SMALLINT)
    private Integer isAgree;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
    @TableField(value = "reason",jdbcType = JdbcType.VARCHAR)
    private String reason;
}
