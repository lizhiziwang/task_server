package com.zsh.task.cache;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class BaseCache_<T>{
    //默认1分钟
    static final TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000*60*60*60 );

    public void put(String key,T t){
        String s = JSONObject.toJSONString(t);
        timedCache.put(key,s);
    }
    public void put(String key,T t,Long time){
        String s = JSONObject.toJSONString(t);
        timedCache.put(key,s,time*1000);
    }

    public T get(String key,final Class<T> c){
        String s = timedCache.get(key);
        return JSONObject.toJavaObject(JSON.parseObject(s), c);
    }
    /**
     * @param time key的存活时间，单位：秒
     * */
    public Boolean setExpire(String key,Long time){
        String s = timedCache.get(key);
        if (StringUtils.isNoneBlank(s)) {
            timedCache.put(key,s,time*1000);
        }
        return false;
    }

    public Boolean delCache(String key){
        boolean b = timedCache.containsKey(key);
        if (b){
            timedCache.remove(key);
            return true;
        }
        return false;
    }

}
