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
@TableName(value = "trad_account")
public class TradAccount implements Serializable {
    @TableId
    private Long id;
    @TableField(value = "create_time",jdbcType = JdbcType.TIMESTAMP)
    private Date createTime;
    @TableField(value = "update_time",jdbcType = JdbcType.TIMESTAMP)
    private Date updateTime;
    @TableField(value = "des_file",jdbcType = JdbcType.VARCHAR)
    private String desFile;
    @TableField(value = "des_text",jdbcType = JdbcType.VARCHAR)
    private String desText;
    @TableField(value = "price",jdbcType = JdbcType.DOUBLE)
    private Double price;
    @TableField(value = "game_type",jdbcType = JdbcType.VARCHAR)
    private String gameType;
    @TableField(value = "game_id",jdbcType = JdbcType.VARCHAR)
    private String gameId;
    @TableField(value = "pub_user",jdbcType = JdbcType.BIGINT)
    private Long pubUser;
    @TableField(value = "is_exist",jdbcType = JdbcType.SMALLINT)
    private Integer isExist;
    @TableField(value = "game_name",jdbcType = JdbcType.VARCHAR)
    private String gameName;
    @TableField(value = "want_num",jdbcType = JdbcType.INTEGER)
    private Integer wantNum;
    @TableField(value = "show_img",jdbcType = JdbcType.VARCHAR)
    private String showImg;
    @TableField(exist = false)
    private Integer isWant;
}
