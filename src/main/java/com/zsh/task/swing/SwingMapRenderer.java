package com.zsh.task.swing;

import com.zsh.task.layer.Feature;
import com.zsh.task.layer.IGeoData;
import com.zsh.task.utils.CoordinateConverter;
import org.locationtech.jts.geom.Geometry;
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
    public void drawFeatures(Graphics2D g2d, List<IGeoData> features, AffineTransform transform) {
        g2d.setTransform(transform); // 应用视图变换

        features.parallelStream().forEach(e->{
            Feature f = (Feature) e;
            Geometry geom = f.getGeometry();
            if(geom instanceof Point){
                Point p = (Point) geom;
//                converter.convertToPixel(p.getX(),p.getY(),g2d.)


                g2d.fillOval((int)p.getX(),(int)p.getY(),5,5);
            }
        });
    }

    @Override
    public void drawUIElements(Graphics2D g2d, double scaleFactor) {

    }
}
