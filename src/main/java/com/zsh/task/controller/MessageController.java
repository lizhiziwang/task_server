package com.zsh.task.controller;


import com.zsh.task.common.Result;
import com.zsh.task.entity.Message;
import com.zsh.task.entity.User;
import com.zsh.task.service.MessageService;
import com.zsh.task.service.UserService;
import com.zsh.task.vo.MessageVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/mes")
public class MessageController {

    @Resource
    MessageService ms;
    @Resource
    UserService us;
    @GetMapping("/his/{id1}/{id2}")
    public Result<List<MessageVo>> hisMes(@PathVariable Long id1, @PathVariable Long id2){
        List<MessageVo> re = new ArrayList<>();
        List<Message> messages = ms.getMessage(id1, id2);
        Map<Long,User> twoUser = us.getTwoUserToMap(id1, id2);

        messages.forEach(e->{
            boolean flag = id1.equals(e.getUser1Id());
            if (!flag) System.out.println(false);
            re.add(new MessageVo(
                    e.getId(),
                    flag?id1:id2,
                    flag?twoUser.get(id1).getAvatar():twoUser.get(id2).getAvatar(),
                    e.getContext())
            );
        });
        return Result.succeed(re);
    }
}
