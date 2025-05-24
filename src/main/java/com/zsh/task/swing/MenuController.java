package com.zsh.task.swing;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Component
public class MenuController extends JMenuBar {

    @Autowired
    Map<String,MenuItemController> menuMap;
//    MenuItemController m;
    @PostConstruct
    public void r(){
        JMenu[] m1={new JMenu("File")
                ,new JMenu("Edit")
                ,new JMenu("Tool")
                ,new JMenu("Help")
                ,new JMenu("theme")
        };
        for (JMenu item : m1){
            add(item);
        }

        //添加文件具体操作
        JMenuItem i1 = new JMenuItem("New");
        JMenuItem i2 = new JMenuItem("Open");
        JMenuItem i3 = new JMenuItem("Open Recent");
        JMenuItem i4 = new JMenuItem("Setting");
        JMenuItem i5 = new JMenuItem("setting");

//        MenuItemController theme = menuMap.get("theme");

        List<MenuItemController> items = menuMap.get("theme").getItems();
        items.forEach(e->m1[4].add(e));

        m1[0].add(i1);
        m1[0].add(i2);
        m1[0].add(i3);
        m1[0].add(i4);
        m1[0].add(i5);

    }
}
