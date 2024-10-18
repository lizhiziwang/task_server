package com.zsh.task.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zsh.task.entity.Message;

import java.util.List;

public interface MessageService extends IService<Message> {
    List<Message> getMessage(Long id1, Long id2,String sendTime);
}
