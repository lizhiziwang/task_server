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
@TableName(value = "user_identity")
public class UserIdentity implements Serializable {

    @TableId
    private Long id;
    @TableField(value = "user_id",jdbcType = JdbcType.BIGINT)
    private Long userId;
    @TableField(value = "identity",jdbcType = JdbcType.SMALLINT)
    private Integer identity;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
}
