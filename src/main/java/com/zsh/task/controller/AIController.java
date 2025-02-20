package com.zsh.task.controller;


import com.zsh.task.utils.HttpRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AIController {

    @PostMapping
    public void chat(ServletOutputStream os,
             @RequestParam(name = "model",required = false,defaultValue = "deepspeek-r1") String model,
             @RequestBody String message) throws IOException {
        HttpRequestUtils.ask_ds_r1_Model(os,message);
    }
}
