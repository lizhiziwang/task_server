package com.zsh.task.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestUtils {

    public static JSONObject get(String url, Map<String,Object> params){
        return JSONObject.parseObject(HttpUtil.get(url, params, 3600));
    }
    public static JSONObject get(String url){
        return JSONObject.parseObject(HttpUtil.get(url, 3600));
    }

//    public static void main(String[] args) {
//        Map<String,Object> params = new HashMap<>();
//        params.put("key","17be85abdc35ac0635cfcfe31fe10936");
//        params.put("location","113.9395,22.5827");
//        get("https://restapi.amap.com/v3/geocode/regeo",params);
//    }
}
