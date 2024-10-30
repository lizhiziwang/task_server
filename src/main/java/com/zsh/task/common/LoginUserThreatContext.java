package com.zsh.task.common;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.zsh.task.entity.User;

public class LoginUserThreatContext{
    private static ThreadLocal<User> CONTEXT = new TransmittableThreadLocal();

    public LoginUserThreatContext() {
    }

    public static User getUser() {
        return (User)CONTEXT.get();
    }

    public static void setUser(User user) {
        CONTEXT.set(user);
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
