package com.zsh.task.vo;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVo {
    private String name;
    private String alia;
    private Integer isOnline;
//    private String createTime;
    private String phone;
    private Integer gender;
    private String birthdayStart;
    private String birthdayEnd;
//    private String updateTime;
}
