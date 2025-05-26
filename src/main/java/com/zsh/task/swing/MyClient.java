package com.zsh.task.swing;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

@Component
public class MyClient extends JFrame implements InitializingBean {
    @Autowired
    MenuController menuController;
    @Autowired
    MapController map;
    @Resource
    LayerController layerController;
    private void initializeGUI() {
        this.setIconImage(new ImageIcon("src/main/resources/logistic.png").getImage());
        setSize(1080,800);
        // 确保在EDT线程执行
        setDefaultLookAndFeelDecorated(true);
        setTitle("GISAPP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //设置布局
        BorderLayout borderLayout = new BorderLayout(2, 2);
        getContentPane().setLayout(borderLayout);

//        MenuController menuController = new MenuController();
        setJMenuBar(menuController);
        JPanel f = new JPanel();

        f.setSize(new Dimension(220,800));

        add(layerController,BorderLayout.WEST);

        add(map,BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        SwingUtilities.invokeLater(this::initializeGUI);


    }
}
