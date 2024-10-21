package com.zsh.task.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

@Data
@Accessors(chain = true)
@TableName(schema = "task",value = "user")
public class User {
    @TableId
    private Long id;
    //用户名
    @TableField(value = "name",jdbcType = JdbcType.VARCHAR)
    private String name;
    @TableField(value = "pwd",jdbcType = JdbcType.VARCHAR)
    //用户密码
    private String pwd;
    @TableField(value = "alia",jdbcType = JdbcType.VARCHAR)
    private String alia;
    @TableField(value = "avatar",jdbcType = JdbcType.VARCHAR)
    private String avatar;
    @TableField(value = "is_online",jdbcType = JdbcType.SMALLINT)
    private int isOnline;
}
