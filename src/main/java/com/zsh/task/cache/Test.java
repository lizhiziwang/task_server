package com.zsh.task.cache;

import com.zsh.task.entity.User;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        BaseCache_<User> cache = new BaseCache_<>();

        User user1 = new User();
        User user2 = new User();
        cache.put("user1",user1.setName("大大怪"),1L);
        cache.put("user2",user2.setName("小小怪"),5L);
        Thread.sleep(2000);
        System.out.println(cache.get("user1",User.class));
        System.out.println(cache.get("user2",User.class));
    }
}
