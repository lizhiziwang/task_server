package com.zsh.task.cache;

import com.zsh.task.config.ThreadPoolConfig;
import com.zsh.task.entity.LoginUser;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.UserMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserCache extends BaseCache_<LoginUser>{
    //kirito
    @Resource
    UserMapper um;
    @Resource
    ThreadPoolConfig tpc;
    // 可使用aop代理自动执行
    public void updateCache(Long id){
        Runnable task = ()->{
            User user = um.selectById(id);
            this.put(String.valueOf(id),new LoginUser(user));
        };

        tpc.poolExecutor().execute(task);

    }
}
