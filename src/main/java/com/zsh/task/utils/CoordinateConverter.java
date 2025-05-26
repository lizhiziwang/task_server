package com.zsh.task.utils;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.awt.*;
import java.awt.geom.Rectangle2D;


public class CoordinateConverter {
    private static final String SOURCE_CRS = "EPSG:4326"; // 源坐标（经纬度）
    private static final String TARGET_CRS = "EPSG:3857"; // 目标投影（Web Mercator，适用于地图瓦片）
//    private final MathTransform transform;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public CoordinateConverter() throws FactoryException {
//        CoordinateReferenceSystem source = CRS.decode(SOURCE_CRS);
//        CoordinateReferenceSystem target = CRS.decode(TARGET_CRS);
//        transform = CRS.findMathTransform(source, target, true); // 启用Lenient模式
    }

    /**
     * 将经纬度坐标（EPSG:4326）转换为像素坐标
     * @param lat 纬度
     * @param lng 经度
     * @param mapBounds 地图范围（目标投影下的坐标范围）
     * @param imageSize 画布尺寸（像素）
     * @return 像素坐标（左上角为原点）
     */
    public Point convertToPixel(double lat, double lng, Rectangle2D mapBounds, Dimension imageSize)
            throws TransformException {
        // 1. 创建源坐标点（EPSG:4326）
        Point sourcePoint = geometryFactory.createPoint(new Coordinate(lng, lat)); // 注意：经纬度顺序为(lng, lat)
        // 4326 to 3857
        double[] doubles = ChinaMapConvert.transformTo3857(sourcePoint.getX(), sourcePoint.getY());
        Point targetPoint = geometryFactory.createPoint(new Coordinate(doubles[0], doubles[1]));

        // 2. 转换为目标投影坐标（如Web Mercator）
//        Point targetPoint = (Point) JTS.transform(sourcePoint, transform);

        // 3. 目标坐标 → 像素坐标（基于地图范围和画布尺寸）
        double xScale = imageSize.width / mapBounds.getWidth();
        double yScale = imageSize.height / mapBounds.getHeight();

        // 计算像素坐标（左上角为原点，Y轴向下递增）
        int pixelX = (int) ((targetPoint.getX() - mapBounds.getMinX()) * xScale);
        int pixelY = imageSize.height -
                (int) ((targetPoint.getY() - mapBounds.getMinY()) * yScale); // 反转Y轴（地理坐标通常左下为原点）

        return geometryFactory.createPoint(new Coordinate(pixelX, pixelY));
    }
}