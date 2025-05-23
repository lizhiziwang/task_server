package com.zsh.task.swing;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MyClient extends JFrame implements InitializingBean {
    @Resource
    LayerController layerController;
    private void initializeGUI() {
        setSize(1080,800);
        // 确保在EDT线程执行
        setDefaultLookAndFeelDecorated(true);
        setTitle("HelloWorldSwing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //设置布局
        BorderLayout borderLayout = new BorderLayout(2, 2);
        getContentPane().setLayout(borderLayout);

        JMenu[] m1={new JMenu("File")
                    ,new JMenu("Edit")
                    ,new JMenu("Tool")
                    ,new JMenu("Help")};
        JMenuBar jmb = new JMenuBar();
        for (JMenu item : m1){
            jmb.add(item);
        }
        //添加文件具体操作
        JMenuItem i1 = new JMenuItem("New");
        JMenuItem i2 = new JMenuItem("Open");
        JMenuItem i3 = new JMenuItem("Open Recent");
        JMenuItem i4 = new JMenuItem("Setting");
        JMenuItem i5 = new JMenuItem("setting");

        m1[0].add(i1);
        m1[0].add(i2);
        m1[0].add(i3);
        m1[0].add(i4);
        m1[0].add(i5);

        setJMenuBar(jmb);
        JPanel f = new JPanel();

        f.setSize(new Dimension(220,800));


        add(layerController,BorderLayout.WEST);

        setVisible(true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        SwingUtilities.invokeLater(this::initializeGUI);


    }
}
