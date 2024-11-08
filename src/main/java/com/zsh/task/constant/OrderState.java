package com.zsh.task.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum OrderState {
    NOPAID(0,"待付款","NOPAID"),
    PAID(1,"已支付","PAID"),
    DELIVERED(2,"已发货","DELIVERED"),
    RECEIVED(3,"已签收","RECEIVED"),
    CANCELED(4,"已取消","CANCELED"),
    REFUNDING(5,"退款中","REFUNDING"),
    REFUNDED(6,"已退款","REFUNDED"),
    COMPLETE(7,"已完成","COMPLETE"),
    UNDELIVER(8,"待发货","UNDELIVER");

    public int num;
    public String name;
    public String code;
    OrderState(int num,String name,String code){
        this.num = num;
        this.name = name;
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public static OrderState findByCode(String code){
        for (OrderState item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        throw new IllegalArgumentException("code is Illegal");
    }

    public static List<Map<String,String>> toList(){
        List<Map<String,String>> re = new ArrayList<>();
        OrderState[] values = values();

        for (OrderState item : values) {
            Map<String ,String> var = new HashMap<>();
            var.put("code",item.code);
            var.put("name", item.name);
            re.add(var);
        }
        return re;
    }

}
