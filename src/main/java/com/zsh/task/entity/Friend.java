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
@TableName(schema = "task",value = "user_relation")
public class Friend {
    @TableId
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @TableField(value = "user1")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long user1Id;
    @TableField(value = "user2")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long user2Id;
    @TableField(value = "user1_name",jdbcType = JdbcType.VARCHAR)
    private String user1Name;
    @TableField(value = "user2_name",jdbcType = JdbcType.VARCHAR)
    private String user2Name;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
}
