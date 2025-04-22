package com.zsh.task.controller;


import com.alibaba.fastjson.JSONObject;
import com.zsh.task.common.Result;
import com.zsh.task.utils.HttpRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AIController {

    @PostMapping
    public void chat(ServletOutputStream os,
//             @RequestParam(name = "model",required = false,defaultValue = "deepspeek-r1") String model,
             @RequestBody String message) throws IOException {
        HttpRequestUtils.ask_ds_r1_Model(os,message);
        os.close();
    }

    @GetMapping("/list/models")
    public Result<?> listModels(){
        String url = "http://localhost:11434/api/tags";
        JSONObject jo = HttpRequestUtils.get(url);
        return Result.succeed(jo);
    }
}
