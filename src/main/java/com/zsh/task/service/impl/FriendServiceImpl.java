package com.zsh.task.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.Friend;
import com.zsh.task.entity.User;
import com.zsh.task.mapper.FriendMapper;
import com.zsh.task.mapper.UserMapper;
import com.zsh.task.service.FriendService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
        implements FriendService {
    @Resource
    UserMapper um;

    @Override
    public List<User> getAllFriend(Long userId) {
        QueryWrapper<Friend> wrapper = new QueryWrapper<>();
        wrapper.eq("user1",userId).or().eq("user2",userId);
        List<Friend> re = this.list(wrapper);
        Set<Long> set = new HashSet<>();
        re.forEach(e-> set.add(userId.equals(e.getUser1Id())?e.getUser2Id():e.getUser1Id()));
        return um.selectByIds(set);
    }

//    public static void main(String[] args) {
//        System.out.println(IdUtil.getSnowflake().nextId());
//    }
}
