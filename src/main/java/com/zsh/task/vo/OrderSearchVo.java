package com.zsh.task.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSearchVo implements Serializable {
    private Long current;
    private Long size;

    private Long createUser;
    //create_time
    private String startTime;
    private String endTime;
    // money
    private Double sumLow;
    private Double sumTop;
    //商品数量
    private Integer proNum;


    private String orderBy = "sum";
    //是否升序
    private Boolean asc = false;
}
