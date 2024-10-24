package com.zsh.task.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(schema = "task",value = "user")
public class User {
    @TableId
    @JSONField(serializeUsing = ToStringSerializer.class)
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
    private Integer isOnline;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "phone",jdbcType = JdbcType.VARCHAR)
    private String phone;
    @TableField(value = "gender",jdbcType = JdbcType.SMALLINT)
    private Integer gender;
    @TableField(value = "birthday",jdbcType = JdbcType.TIMESTAMP)
    private Date birthday;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;

    @TableField(exist = false)
    private Integer mesCount;
    @TableField(exist = false)
    private String lastMess;
}
