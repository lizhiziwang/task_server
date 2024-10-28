package com.zsh.task.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum GameType {

    MOBA(1,"MOBA","多人联机在线竞技游戏"),
    FPS(2,"FPS","第一人称射击类游戏"),
    ACT(3,"ACT","动作游戏"),
    STG(4,"STG","射击游戏"),
    FTG(5,"FTG","格斗游戏"),
    AVG(6,"AVG","冒险游戏"),
    SIM(7,"SIM","模拟游戏"),
    RPG(8,"RPG","角色扮演游戏"),
    SG(9,"SG","策略游戏"),
    MG(10,"MG","音乐游戏"),
    CG(11,"CG","休闲游戏"),
    SG_(12,"SG_","体育游戏"),
    RG(13,"RG","竞速游戏");


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
            RE.add(map);
        }
        return RE;
    }
}
