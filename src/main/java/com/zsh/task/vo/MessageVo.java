package com.zsh.task.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MessageVo {
    private Long id;
    private Long uId;
    private String imgUrl;
    private String msg;
}
