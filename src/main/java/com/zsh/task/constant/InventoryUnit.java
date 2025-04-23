package com.zsh.task.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum InventoryUnit {
    JIN(1,"JIN","斤"),
    KG(2,"KG","千克"),
    GE(3,"GE","个"),
    KE(4,"KE","ke");
//    RPG(8,"RPG","角色扮演"),
//    SG(9,"SG","策略"),
//    MG(10,"MG","音乐"),
//    CG(11,"CG","休闲"),
//    SG_(12,"SG_","体育"),
//    RG(13,"RG","竞速");



    final String code;
    final int num;
    final String alia;

    InventoryUnit(int num,String code,String alia){
        this.num = num;
        this.code = code;
        this.alia = alia;
    }
    public String getCode(){
        return this.code;
    }
    public String getAlia(){
        return this.alia;
    }

    public static InventoryUnit findByName(String name){
        InventoryUnit[] values = values();
        for (InventoryUnit item : values) {
            if (item.alia.equals(name)){
                return item;
            }
        }
        throw new IllegalArgumentException("name is illegal");
    }
    public static InventoryUnit findByCode(String code){
        InventoryUnit[] values = values();
        for (InventoryUnit item : values) {
            if (item.code.equals(code)){
                return item;
            }
        }
        throw new IllegalArgumentException("name is illegal");
    }
    public static List<Map<String,String>> toList(){
        List<Map<String,String>> RE = new ArrayList<>();
        InventoryUnit[] items = values();

        for(InventoryUnit var :items){
            Map<String,String> map = new HashMap<>();
            map.put("code",var.code);
            map.put("name",var.alia);
            map.put("num", String.valueOf(var.num));
            RE.add(map);
        }
        return RE;
    }
}
