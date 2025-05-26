package com.zsh.task.swing;

import com.zsh.task.layer.IGeoData;
import com.zsh.task.layer.ILayer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LayerController extends JPanel {


    private final List<ILayer> layers = new ArrayList<>();

    private final JScrollPane var = new JScrollPane();

    public List<ILayer> getLayers() {
        return layers;
    }

    public void addLayer(ILayer layer){
        layers.add(layer);
        updateLayerCards();
    }

    @PostConstruct
    public void init(){
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //todo 处理文件是否可用
        JLabel tile = new JLabel("layer");

        tile.putClientProperty("FlatLaf.styleClass","h3");
        tile.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        ImageIcon imageIcon = new ImageIcon("src/main/resources/图层管理.png");
        Image image = imageIcon.getImage();
        Image scaledInstance = image.getScaledInstance(18, 18, Image.SCALE_SMOOTH);
        ImageIcon imageIcon1 = new ImageIcon(scaledInstance);
        tile.setIcon(imageIcon1);

//        a.add(tile,BorderLayout.CENTER);
        add(tile);

        // 关键：设置 BoxLayout 垂直布局
        var.setPreferredSize(new Dimension(220,800));
//        var.setVerticalScrollBar(new JScrollBar());
        updateLayerCards();

        add(var);
    }
    private void updateLayerCards() {
        // 获取内容面板
        JPanel contentPanel = getContentPanel();
        contentPanel.removeAll();

        if (!layers.isEmpty()) {
            for (ILayer layer : layers) {
                LayerCard card = new LayerCard(layer);
                card.addShowLayerListener(new ShowLayerListener() {
                    @Override
                    public void onShowLayer(ShowLayerEvent event) {
                        System.out.println(event.getMessage());
                    }

                    @Override
                    public void onCloseLayer(ShowLayerEvent event) {
                        // 处理关闭图层的逻辑
                        layers.remove(layer);
                        updateLayerCards();
                    }
                });
                contentPanel.add(card);
            }
        }

        // 刷新UI
        SwingUtilities.invokeLater(() -> {
            contentPanel.revalidate();
            contentPanel.repaint();
        });
    }

    private JPanel getContentPanel() {
        java.awt.Component view = var.getViewport().getView();
        if (view instanceof JPanel) {
            return (JPanel) view;
        }

        // 如果没有内容面板，创建一个使用垂直布局的面板
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        var.setViewportView(panel);
        return panel;
    }

}
