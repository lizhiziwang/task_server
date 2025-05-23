package com.zsh.task;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.zsh.task.swing.MyClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

//@MapperScan(value = {"com.zsh.task.mapper"})
@SpringBootApplication
public class TaskApplication {

    public static void main(String[] args) throws Exception {
//        SpringApplication.run(TaskApplication.class, args);
//        System.out.println("启动成功，Sa-Token 配置如下：" + SaManager.getConfig());

        System.setProperty("flatlaf.debug", "true");
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        // 设置全局属性
        UIManager.put("Button.arc", 10);        // 圆角按钮（值为 0 时为直角）
        UIManager.put("Component.focusWidth", 1); // 焦点边框宽度
        UIManager.put("Component.innerFocusWidth", 0); // 内部焦点边框宽度
        UIManager.put("ScrollBar.showButtons", true); // 显示滚动条按钮
        UIManager.put("TabbedPane.tabClosable", true); // 标签页可关闭
        UIManager.put("TitlePane.showIcon", false);

        ConfigurableApplicationContext context = new SpringApplicationBuilder(TaskApplication.class)
                .headless(false) // 关键：禁用headless模式
                .run(args);

        context.getBean(MyClient.class); // 触发GUI初始化
    }

}
