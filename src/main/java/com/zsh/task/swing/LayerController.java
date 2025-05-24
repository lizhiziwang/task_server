package com.zsh.task.swing;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.List;

@Component
public class LayerController extends JPanel {

    @Value("${layer.file.path}")
    String path;

    private List<String> urls;

    @PostConstruct
    public void init(){
        String[] p = path.split(",");

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel a = new JPanel();
        a.setPreferredSize(new Dimension(220,20));
        a.setLayout(new BorderLayout());


        //todo 处理文件是否可用
        JLabel tile = new JLabel("layer");
        tile.setPreferredSize(new Dimension(220,30));

        tile.putClientProperty("FlatLaf.styleClass","h3");
        tile.setHorizontalAlignment(SwingConstants.CENTER); // 水平居中
        ImageIcon imageIcon = new ImageIcon("src/main/resources/图层管理.png");
        Image image = imageIcon.getImage();
        Image scaledInstance = image.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        tile.setIcon(new ImageIcon(scaledInstance));

        a.add(tile,BorderLayout.CENTER);
        add(a);

        JScrollPane var=new JScrollPane();
        // 关键：设置 BoxLayout 垂直布局
        var.setPreferredSize(new Dimension(220,800));
//        var.setVerticalScrollBar(new JScrollBar());

        JList<JLabel> list=new JList<>();
        list.putClientProperty("FlatLaf.styleClass","large");


        //限制只能选择一个元素
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        var.setViewportView(list);
        JLabel[] listData=new JLabel[100];


        for (int i = 0; i < 100; i++) {
            listData[i] = new JLabel();
            listData[i].setText("这是列表框的第"+(i+1)+"个元素~");
        }
        list.setListData(listData);
        add(var);
    }


}
