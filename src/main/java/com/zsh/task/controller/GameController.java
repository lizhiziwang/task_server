package com.zsh.task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.common.Result;
import com.zsh.task.constant.GameType;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.service.WantAccountService;
import com.zsh.task.vo.AccountSelectVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/game")
public class GameController {
    @Resource
    WantAccountService was;
    @Resource
    TradAccountService tas;
    @GetMapping("/type")
    public Result<List<Map<String,String>>> gameType(){
        return Result.succeed(GameType.toList().stream().sorted(Comparator.comparing(e -> Integer.valueOf(e.get("num")))).collect(Collectors.toList()));
    }
    @PostMapping("/page")
    public Result<Page<TradAccount>> selectAccountPage(@RequestBody AccountSelectVo params){
        return Result.succeed(tas.selectAccountPage(params));
    }
    @PostMapping("/want")
    public Result<Boolean> addLike(@RequestParam(name = "userId") Long userId,
                                   @RequestParam(name = "accId") Long accId){
        return Result.succeed(was.addLike(userId, accId));
    }


}
