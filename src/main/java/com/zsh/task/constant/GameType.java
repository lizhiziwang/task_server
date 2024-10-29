package com.zsh.task.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GameType {

    ALL(0,"ALL","全部"),
    MOBA(1,"MOBA","MOBA"),
    FPS(2,"FPS","FPS"),
    ACT(3,"ACT","动作"),
    STG(4,"STG","射击"),
    FTG(5,"FTG","格斗"),
    AVG(6,"AVG","冒险"),
    SIM(7,"SIM","模拟"),
    RPG(8,"RPG","角色扮演"),
    SG(9,"SG","策略"),
    MG(10,"MG","音乐"),
    CG(11,"CG","休闲"),
    SG_(12,"SG_","体育"),
    RG(13,"RG","竞速"),
    MY(14,"MY","我的");


    final String code;
    final int num;
    final String alia;

    GameType(int num,String code,String alia){
        this.num = num;
        this.code = code;
        this.alia = alia;
    }
    public String getCode(){
        return this.code;
    }
    public static List<Map<String,String>> toList(){
        List<Map<String,String>> RE = new ArrayList<>();
        GameType[] items = values();

        for(GameType var :items){
            Map<String,String> map = new HashMap<>();
            map.put("code",var.code);
            map.put("name",var.alia);
            map.put("num", String.valueOf(var.num));
            RE.add(map);
        }
        return RE;
    }
}
