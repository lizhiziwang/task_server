package com.zsh.task.swing;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Component
public class MapController extends JPanel {
    @Autowired
    LayerController layerController;
    @Autowired
    SwingMapRenderer renderer;

    // 视图变换状态
    private double scale = 1.0;
    private Point dragStart = null;
    private double translateX = 0;
    private double translateY = 0;
    private double baseTranslateX = 0;
    private double baseTranslateY = 0;

    // 调试标志
    private static final boolean DEBUG = false;

    // 地图原点（左下角为原点）
    private static final int ORIGIN_X = 0;
    private static final int ORIGIN_Y = 0;

    private Graphics2D g;

    @PostConstruct
    public void init() {
        // 启用双缓冲以减少闪烁
        setDoubleBuffered(true);

        // 设置面板为可聚焦
        setFocusable(true);
        requestFocusInWindow();

        // 添加鼠标右键拖动支持
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (DEBUG) System.out.println("Mouse pressed: " + e.getButton());

                // 仅处理右键
                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragStart = e.getPoint();
                    if (DEBUG) System.out.println("Right button pressed at: " + dragStart);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (DEBUG) System.out.println("Mouse released: " + e.getButton());

                if (SwingUtilities.isLeftMouseButton(e)) {
                    dragStart = null;

                    // 更新基础平移量
                    baseTranslateX += translateX;
                    baseTranslateY += translateY;
                    translateX = 0;
                    translateY = 0;

                    if (DEBUG) System.out.println("Right button released. New translation: " +
                            baseTranslateX + ", " + baseTranslateY);
                }
            }
        };

        // 添加鼠标拖动事件
        MouseMotionAdapter motionAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (DEBUG) System.out.println("Mouse dragged: " + e.getPoint());

                if (dragStart != null && SwingUtilities.isLeftMouseButton(e)) {
                    // 计算拖动距离
                    Point dragEnd = e.getPoint();

                    // 修正拖动方向 - 这里是关键修改
                    translateX = (dragEnd.x - dragStart.x) / scale;
                    translateY = (dragEnd.y - dragStart.y) / scale;
                    // 反转平移方向
                    translateX = -translateX;
                    translateY = -translateY;

                    if (DEBUG) System.out.println("Dragging: deltaX=" + translateX + ", deltaY=" + translateY);

                    repaint();
                }
            }
        };

        // 添加鼠标滚轮缩放支持
        MouseWheelListener wheelListener = e -> {
            if (DEBUG) System.out.println("Mouse wheel event: rotation=" + e.getWheelRotation() +
                    ", point=" + e.getPoint());

            // 获取鼠标位置作为缩放中心
            Point mouse = e.getPoint();

            // 计算缩放前鼠标在内容中的位置
            double oldX = (mouse.x - getWidth()/2) / scale - baseTranslateX;
            double oldY = (mouse.y - getHeight()/2) / scale - baseTranslateY;

            // 处理滚轮事件，调整缩放比例
            if (e.getWheelRotation() < 0) {
                scale *= 1.1; // 放大
            } else {
                scale /= 1.1; // 缩小
                // 限制最小缩放比例
                scale = Math.max(0.1, scale);
            }

            // 计算缩放后鼠标在内容中的新位置
            double newX = (mouse.x - getWidth()/2) / scale - baseTranslateX;
            double newY = (mouse.y - getHeight()/2) / scale - baseTranslateY;

            // 调整平移量，使缩放中心保持在鼠标位置
            baseTranslateX += newX - oldX;
            baseTranslateY += newY - oldY;

            if (DEBUG) System.out.println("New scale: " + scale + ", translation: " +
                    baseTranslateX + ", " + baseTranslateY);

            repaint();
        };

        // 注册事件监听器
        addMouseListener(mouseAdapter);
        addMouseMotionListener(motionAdapter);
        addMouseWheelListener(wheelListener);

        // 添加组件大小变化监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (DEBUG) System.out.println("Component resized to: " + getWidth() + "x" + getHeight());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (DEBUG) System.out.println("Painting component: width=" + getWidth() + ", height=" + getHeight() +
                ", scale=" + scale + ", translation=(" +
                (baseTranslateX + translateX) + ", " + (baseTranslateY + translateY) + ")");

        // 设置抗锯齿
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();



        AffineTransform viewTransform = new AffineTransform();

        // 构建视图变换：平移到中心 → 缩放 → 应用偏移
        viewTransform.translate(width/2, height/2);
        viewTransform.scale(scale, scale);
        viewTransform.translate(-(baseTranslateX + translateX), -(baseTranslateY + translateY));

        g2d.setTransform(viewTransform); // 应用视图变换

        int x = getX();
        int y = getY();
        log.info("x;"+x);
        log.info("y;"+y);
        g2d.setColor(Color.RED);
        g2d.fillOval(x,y,5,5);
        // 设置背景色
        setBackground(Color.black);

        // 绘制网格线，帮助可视化变换效果
        drawGrid(g2d);
        layerController.getLayers().parallelStream().forEach(e->{
            renderer.drawFeatures(g2d,e.features(),viewTransform);
        });

        // 恢复原始变换
        g2d.setTransform(new AffineTransform());
    }

    // 绘制网格线，帮助可视化变换效果
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1f / (float)scale));

        // 绘制X轴和Y轴
        g2d.setColor(Color.WHITE);
        g2d.drawLine(ORIGIN_X, -1000, ORIGIN_X, 1000); // Y轴
        g2d.drawLine(-1000, ORIGIN_Y, 1000, ORIGIN_Y); // X轴

        // 绘制网格
        g2d.setColor(Color.GRAY);
        for (int x = -1000; x <= 1000; x += 50) {
            g2d.drawLine(x, -1000, x, 1000);
        }
        for (int y = -1000; y <= 1000; y += 50) {
            g2d.drawLine(-1000, y, 1000, y);
        }
    }

    // 获取当前视图状态，用于调试
    public String getViewState() {
        return String.format("Scale: %.2f, Translation: (%.2f, %.2f), BaseTranslation: (%.2f, %.2f)",
                scale, translateX, translateY, baseTranslateX, baseTranslateY);
    }
}
