package com.zsh.task.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsh.task.entity.Message;
import com.zsh.task.mapper.MessageMapper;
import com.zsh.task.service.MessageService;
import com.zsh.task.vo.UnreadVo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MessageServiceImpl  extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public List<Message> getMessage(Long id1, Long id2,String sendTime) {
        Long[] var = {id1, id2};
        QueryWrapper<Message> wrapper = new QueryWrapper<>();

        if (!StringUtils.isBlank(sendTime)) {
            Date send = DateUtil.parse(sendTime);
            wrapper.lt("send_time",send);
        }
        wrapper.in("user1_id", (Object) var)
                .or()
                .in("user2_id", (Object) var)
                .orderByDesc("send_time")
                .last("limit 50");

        List<Message> var2 = this.list(wrapper);

        return var2.stream().sorted(Comparator.comparing(Message::getSendTime)).collect(Collectors.toList());
    }

    @Override
    public List<UnreadVo> selectUnread(Long userId) {
        return baseMapper.selectUnread(userId);
    }


}
