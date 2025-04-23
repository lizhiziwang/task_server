package com.zsh.task.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsh.task.common.LoginUserThreatContext;
import com.zsh.task.common.Result;
import com.zsh.task.constant.GameType;
import com.zsh.task.entity.MoneyRecord;
import com.zsh.task.entity.TradAccount;
import com.zsh.task.service.MoneyRecordService;
import com.zsh.task.service.TradAccountService;
import com.zsh.task.service.UserService;
import com.zsh.task.service.WantAccountService;
import com.zsh.task.utils.TimeUtils;
import com.zsh.task.vo.AccountSelectVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    UserService us;
    @Resource
    TradAccountService tas;
    @Resource
    MoneyRecordService mrs;
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
                    .setWantNum(0)
                    .setIsExist(1)
                    .setCreateTime(new Date())
                    .setPubUser(LoginUserThreatContext.getUser().getId());

        }
        return Result.succeed(tas.saveOrUpdate(ta.setUpdateTime(new Date())));
    }
    @PostMapping("/money/add")
    public Result<MoneyRecord> save(@RequestParam(name = "m") Double m,
                                    @RequestParam(name = "type") String type){
        Long cuUserId = LoginUserThreatContext.getUser().getId();
        Long id = IdUtil.getSnowflakeNextId();
        MoneyRecord mr = new MoneyRecord();
        mr.setId(id)
                .setOperator(cuUserId)
                .setOperatorAmount(m)
                .setOperatorType(type)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return mrs.save(mr)?Result.succeed(mr):Result.failed("充值失败！请稍后再试");
    }

    // 购物车列表
    @GetMapping("/want/page")
    public Result<Page<TradAccount>> wantPage(@RequestParam(name = "current") Long current,
                                              @RequestParam(name = "size") Long size){

        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        qw.eq("t2.is_want",1)
                        .orderByDesc("t2.create_time");

        return Result.succeed(tas.selectPage(new Page<>(current,size),qw));
    }

    @PostMapping("/list/ids")
    public Result<List<TradAccount>> getByList(@RequestBody Long [] ids){
        QueryWrapper<TradAccount> qw = new QueryWrapper<>();
        qw.in("t1.id",ids)
                .orderByDesc("t2.create_time");

        return Result.succeed(tas.selectPage(new Page<>(1,20),qw).getRecords());
    }
    @GetMapping("/type/count")
    public Result<List<Map<String, Object>>> typeCount(@RequestParam(name = "pubUser") Long pubUser,
                                       @RequestParam(name = "pubTime") String pubTime){
        List<Map<String, Object>> re = tas.typeCount(pubUser, pubTime);

        re.forEach(e->{
            String name = e.get("name").toString();
            String alia = GameType.findByCode(name).getAlia();
            e.put("name",alia);
        });
        return Result.succeed(re);
    }
    @PostMapping("/my/get")
    public Result<Page<?>> myTranGet(@RequestBody Map<String,Object> param) {
        return Result.succeed(tas.selectPage_2(param));
    }
    @PostMapping("/de/{id}")
    public Result<Boolean> deleteTra(@PathVariable Long id){
        return Result.succeed(tas.removeById(id));
    }

    @GetMapping("/sandiantu")
    public Result<JSONArray> allData(@RequestParam(name = "pubTime") String pubTime,
                                     @RequestParam(name = "pubUser")Long pubUser){
        JSONArray re = new JSONArray();

        QueryWrapper<TradAccount> qw = new QueryWrapper<>();

        if(pubUser != 0L){
            qw.eq("pub_user",pubUser);
        }
        if (StringUtils.isNoneBlank(pubTime)) {
            Date parse = DateUtil.parse(pubTime);
            qw.gt("create_time",parse);

        }else {
            Date date = TimeUtils.last30dayDate();
            qw.gt("create_time",date);
        }

        List<TradAccount> var = tas.list(qw);

        for (TradAccount item : var) {
            item.setGameType(GameType.findByCode(item.getGameType()).getAlia());
        }

        Map<String, List<TradAccount>> collect = var.stream().collect(
                Collectors.groupingBy(TradAccount::getGameType)
        );

        collect.forEach((k,v)->{
            List<Object[]> var2 = new ArrayList<>();
            for (TradAccount item : v) {
                var2.add(new Object[]{item.getWantNum(), item.getPrice(),item.getId()});
            }
            Map<String,Object> map = new HashMap<>();
            map.put("name",k);
            map.put("data",var2);
            re.add(map);
        });
        return Result.succeed(re);
    }
    @GetMapping("/erecsxz")
    public Result<JSONArray> coordateData(){
        JSONArray ja = new JSONArray();

        List<Map<String, Object>> data = us.selectAllCity();
        data.forEach(e->{
            if(e.get("lon") == null|| e.get("lat") == null)
                return;
            JSONObject jo = new JSONObject();
            jo.put("name",e.get("name"));
            jo.put("value",new Object[]{ e.get("lon"),  e.get("lat"),Integer.parseInt(e.get("count").toString())});
            ja.add(jo);
        });
        return Result.succeed(ja);
    }
}