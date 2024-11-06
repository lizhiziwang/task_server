package com.zsh.task.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AccountSelectVo {
    //类型
    private String gameType;
    //描述
    private String desText;
    private Long pubUser;
    private Integer wantNum;

    private String startTime;
    private String endTime;

    private Double lowPrice;
    private Double upPrice;

    private int current = 1;
    private int size = 20;

    private boolean desc = true;
    private String orderBy = "want_num";

}
