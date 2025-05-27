package com.zsh.task.swing;

import com.zsh.task.layer.Feature;
import com.zsh.task.layer.IGeoData;
import com.zsh.task.utils.ChinaMapConvert;
import com.zsh.task.utils.CoordinateConverter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;

@Component
public class SwingMapRenderer implements MapRenderer{
    private CoordinateConverter converter = new CoordinateConverter();

    public SwingMapRenderer() throws FactoryException {
    }

    @Override
    public void drawBackground(Graphics2D g2d, Rectangle viewPort) {

    }

    @Override
    public void drawFeatures(Graphics2D g2d, List<IGeoData> features, AffineTransform transform, int width, int height) {
        g2d.setColor(Color.RED);

        try {

            features.parallelStream().forEach(feature -> {
                Feature f = (Feature) feature;
                Geometry geom = f.getGeometry();

                if (geom instanceof Point) {
                    Point p = (Point) geom;
                    double[] coords = ChinaMapConvert.transformTo3857(p.getX(), p.getY());
//                    double[] pixelCoords = ChinaMapConvert.mercatorxToPixel(coords[0], coords[1], width, height);

//                    // 临时恢复原始变换（在屏幕空间绘制）
//                    g2d.setTransform(originalTransform);
                    // 计算固定大小的点（6x6像素不受缩放影响）
                    int pointSize = 6;



                    // Synchronize drawing operations as Graphics2D is not thread-safe
                    synchronized (g2d) {
                        g2d.fillOval(
                                (int) coords[0] - pointSize/2,  // 中心对齐
                                (int) coords[1] - pointSize/2,
                                pointSize,
                                pointSize
                        );

                        // 恢复地图变换
                        g2d.setTransform(transform);
                    }

                } else if (geom instanceof LineString) {
                    LineString line = (LineString) geom;
                    Coordinate[] coordinates = line.getCoordinates();
                    int[] xPoints = new int[coordinates.length];
                    int[] yPoints = new int[coordinates.length];

                    // Convert all coordinates
                    for (int i = 0; i < coordinates.length; i++) {
                        double[] transformed = ChinaMapConvert.transformTo3857(coordinates[i].x, coordinates[i].y);
                        double[] pixel = ChinaMapConvert.mercatorxToPixel(transformed[0], transformed[1], width, height);
                        xPoints[i] = (int) pixel[0];
                        yPoints[i] = (int) pixel[1];
                    }

                    // Draw the line
                    synchronized (g2d) {
                        g2d.drawPolyline(xPoints, yPoints, coordinates.length);
                    }
                }
                // Add more geometry types as needed (Polygon, etc.)
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void drawUIElements(Graphics2D g2d, double scaleFactor) {

    }
}
