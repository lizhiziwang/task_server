package com.zsh.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.User;
import com.zsh.task.vo.UserVo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface UserService extends IService<User> {

    Map<String, Object> doLogin(String name, String pwd);
    User getByName(String name);
    /**
     * @param userId
     * @return 布尔
     * */
    Boolean downLine(Long userId);

    Page<User> searchUsers(UserVo vo, Long size, Long current);

    List<User> findFriends(Long userId,String userName);

    Map<Long,User> getTwoUserToMap(Long id1, Long id2);
}
