package com.zsh.task.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo {
    private Long id;
    private Long uId;
    private String imgUrl;
    private String msg;
}
