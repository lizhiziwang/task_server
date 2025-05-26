package com.zsh.task.swing;

import com.zsh.task.layer.IGeoData;
import com.zsh.task.layer.ILayer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class LayerCard extends JPanel {
    private final List<ShowLayerListener> listeners = new ArrayList<>();
    private final ILayer layer;
    private JCheckBox checkBox;

    public LayerCard(ILayer layer) {
        this.layer = layer;
        initComponents();
        setupEventListeners();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(220,60));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        // 复选框是否显示
        checkBox = new JCheckBox();
        checkBox.setSelected(layer.getVisible()); // 假设ILayer有isVisible()方法
        checkBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // 右侧添加10px边距


        // 图层名
        JLabel name = new JLabel(layer.getName());
        name.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        add(checkBox);
        add(name);
    }

    private void setupEventListeners() {
        // 复选框点击事件
        checkBox.addActionListener(e -> {
            layer.setVisible(checkBox.isSelected()); // 假设ILayer有setVisible()方法
            fireCustomEvent(checkBox.isSelected());
        });

        // 面板点击事件 - 切换复选框状态
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    checkBox.setSelected(!checkBox.isSelected());
                    layer.setVisible(checkBox.isSelected());
                    fireCustomEvent(checkBox.isSelected());
                }
            }
        });
    }

    // 添加监听器
    public void addShowLayerListener(ShowLayerListener listener) {
        listeners.add(listener);
    }

    // 移除监听器
    public void removeShowLayerListener(ShowLayerListener listener) {
        listeners.remove(listener);
    }

    // 触发事件
    protected void fireCustomEvent(boolean message) {
        ShowLayerEvent event = new ShowLayerEvent(this, message);
        for (ShowLayerListener listener : listeners) {
            listener.onShowLayer(event);
        }
        log.debug("触发图层事件: {}", message);
    }

    // Getter方法
    public ILayer getLayer() {
        return layer;
    }

    public boolean isLayerVisible() {
        return checkBox.isSelected();
    }
}