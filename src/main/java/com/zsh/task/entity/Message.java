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
@TableName(schema = "task",value = "message")
public class Message {
    @TableId
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    //发送人
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "user1_id",jdbcType = JdbcType.BIGINT)
    private Long user1Id;
    //接收人
    @JSONField(serializeUsing = ToStringSerializer.class)
    @TableField(value = "user2_id",jdbcType = JdbcType.BIGINT)
    private Long user2Id;
    private String context;
    @TableField(value = "send_time",jdbcType = JdbcType.TIMESTAMP)
    private Date sendTime;

    private String type;
    @TableField(value = "is_read",jdbcType = JdbcType.SMALLINT)
    private Integer isRead;
}