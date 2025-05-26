package com.zsh.task.swing;

import com.zsh.task.layer.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

@Component
public class MenuController extends JMenuBar {
    @Autowired
    MyClient client;
    @Autowired
    LayerController layerController;
    @Autowired
    Map<String,FileAnalyze> fileAnalyze;

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
        openFile(i2);
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

    private void openFile(JMenuItem item){
        item.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = fileChooser.showOpenDialog(client);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                String name = file.getName();
                String fileType = name.split("\\.")[1].toUpperCase();
                FileAnalyze analyze = fileAnalyze.get(fileType);

                ILayer layer = new ShpLayer();
                analyze.analyze(file.getPath(),layer);
                layerController.addLayer(layer);


                System.out.println("选中的文件夹: " + file.getAbsolutePath());
                // 你可以在这里进行后续操作，比如显示文件夹内容
            }
        });
    }
}
