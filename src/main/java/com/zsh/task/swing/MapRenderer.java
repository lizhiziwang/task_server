package com.zsh.task.swing;

import com.zsh.task.layer.IGeoData;

import java.awt.*;
import java.awt.geom.AffineTransform;


/**
 * 地图绘制接口，基于点坐标（Point）进行图形渲染
 */
public interface MapRenderer {

    /**
     * 绘制地图背景（如网格、坐标系）
     * @param g2d 图形上下文
     * @param viewPort 视口范围（当前可见区域）
     */
    void drawBackground(Graphics2D g2d, Rectangle viewPort);

    /**
     * 绘制地理要素（如点、线、面）
     * @param g2d 图形上下文
     * @param features 地理要素集合
     * @param transform 视图变换（缩放、平移）
     */
    void drawFeatures(Graphics2D g2d, java.util.List<IGeoData> features, AffineTransform transform,int width,int height);

    /**
     * 绘制标注文本（如地名、坐标）
     * @param g2d 图形上下文
     * @param annotations 标注集合
     * @param transform 视图变换
     */
//    void drawAnnotations(Graphics2D g2d, List<Annotation> annotations, AffineTransform transform);

    /**
     * 绘制比例尺和指北针
     * @param g2d 图形上下文
     * @param scaleFactor 比例尺因子（1像素代表的实际距离）
     */
    void drawUIElements(Graphics2D g2d, double scaleFactor);

    /**
     * 地理要素数据结构（示例）
     */
    record GeoFeature(Point location, String type, Color color) {}

    /**
     * 标注数据结构（示例）
     */
    record Annotation(Point location, String text, Font font, Color color) {}
}