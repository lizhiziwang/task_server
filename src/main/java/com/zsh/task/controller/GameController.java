package com.zsh.task.controller;

import com.zsh.task.common.Result;
import com.zsh.task.constant.GameType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GameController {
    @GetMapping("/type")
    public Result<List<Map<String,String>>> gameType(){
        return Result.succeed(GameType.toList());
    }
}
