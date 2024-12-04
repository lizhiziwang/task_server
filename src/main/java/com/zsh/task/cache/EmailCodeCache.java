package com.zsh.task.cache;

import org.springframework.stereotype.Component;

@Component
public class EmailCodeCache extends BaseCache_<String>{


    public void putCode(String key,String code,long time){
        timedCache.put(key,code,time);
    }

    public String getCode(String key){
        return timedCache.get(key);
    }

}
