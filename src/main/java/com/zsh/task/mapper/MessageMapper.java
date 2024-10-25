package com.zsh.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsh.task.entity.Message;
import com.zsh.task.vo.UnreadVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    List<UnreadVo> selectUnread(@Param("userId") Long userId);
    List<Message> getMessage(@Param("id1") Long id1,
                             @Param("id2") Long id2);
}
