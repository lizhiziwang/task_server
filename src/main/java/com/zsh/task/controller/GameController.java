package com.zsh.task.controller;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.common.Result;
import com.zsh.task.constant.GameType;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.service.WantAccountService;
import com.zsh.task.vo.AccountSelectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/game")
public class GameController {
    @Resource
    WantAccountService was;
    @Resource
    TradAccountService tas;
    @Value("${game.file.path}")
    String filePath;
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

    @PostMapping("/files")
    public Result<List<String>> uploadFiles(MultipartFile [] files){
        if (files==null||files.length==0) {
            return Result.failed("上传文件列表为空！");
        }

        List<String> re = new ArrayList<>();
        for (MultipartFile item:files){
            String[] ofn = item.getOriginalFilename().split("\\.");

            String suffix = ofn[1];
            String fileName = IdUtil.getSnowflakeNextId()+"."+suffix;
            try(FileOutputStream fos = new FileOutputStream(filePath+ File.separator+fileName)){

                byte[] data = new byte[1024];

                InputStream is = item.getInputStream();
                while (-1!=(is.read(data))){
                    fos.write(data);
                }
                fos.flush();
                re.add(fileName);
            } catch (IOException e) {
                log.error("文件写入失败：", e);
                return Result.failed("文件写入失败：" + e.getMessage());
            }
        }
        return Result.succeed(re);

    }


    @PostMapping
    public Result<Boolean> save(@RequestBody TradAccount ta){
        ta.setGameType(GameType.findByName(ta.getGameType()).name());
        if (ta.getId() == null) {
            ta.setId(IdUtil.getSnowflakeNextId())
                    .setCreateTime(new Date());

        }
        return Result.succeed(tas.saveOrUpdate(ta.setUpdateTime(new Date())));
    }
}
