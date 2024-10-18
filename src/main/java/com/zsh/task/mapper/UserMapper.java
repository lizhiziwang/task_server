package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsh.task.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
