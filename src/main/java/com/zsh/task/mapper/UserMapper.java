package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.entity.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {
    int updateByPrimaryKeySelective(User user);
    List<User> selectByIds(@Param("ids") Collection<?> collection);
    List<User> findFriends(@Param("userId")Long userId,
                           @Param("name")String name);
    @MapKey("id")
    Map<Long,User> getTwoUserToMap(@Param("id1")Long id1,
                                   @Param("id2") Long id2);

    Page<User> myPage(Page<User> page , @Param(Constants.WRAPPER)QueryWrapper<User> qw);
    int purseUpOrDown(@Param("var1")double var1,@Param("id")Long id);
    List<Map<String,Object>> getRequestNoAgree(@Param("receiver") Long receiver);

    List<Map<String,Object>> selectAllCity();
}
