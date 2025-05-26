package com.zsh.task.layer;

import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("SHP")
public class ShpAnalyze implements FileAnalyze{
    private ILayer current;
    private FeatureCollection<SimpleFeatureType, SimpleFeature> read(String input, List<AttributeDescriptor> col){

        try{
            File file = new File(input);
            current.setName(file.getName());

            ShapefileDataStore dataStore= new ShapefileDataStore(file.toURI().toURL());
            try{
                CoordinateReferenceSystem coordinateReferenceSystem = dataStore.getSchema().getCoordinateReferenceSystem();
                if(null == coordinateReferenceSystem){
                    current.setSRID("SRID=4326");
                }else {
                    current.setSRID(coordinateReferenceSystem.toWKT());
                }
            }catch (Exception e){
                current.setSRID("SRID=4326");
            }

            dataStore.setCharset(dataStore.getCharset());
//            dataStore.setCharset(Charset.forName("GBK"));

            String typeName= dataStore.getTypeNames()[0];
            List<AttributeDescriptor> var = dataStore.getFeatureSource(typeName).getSchema().getAttributeDescriptors();
            col.addAll(var);

            FeatureSource<SimpleFeatureType, SimpleFeature> source= dataStore.getFeatureSource(typeName);

            dataStore.dispose();
            return source.getFeatures();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void analyze(String path, ILayer layer) {
        current = layer;

        List<AttributeDescriptor> col = new ArrayList<>();

        FeatureCollection<SimpleFeatureType, SimpleFeature> data = read(path, col);

        assert data != null;
        try(FeatureIterator<SimpleFeature> iterator = data.features()){
            while (iterator.hasNext()){
                Feature feature = new Feature();
                SimpleFeature f = iterator.next();
                Map<String,String> attribute = new HashMap<>();

                for (AttributeDescriptor attributeDescriptor : col) {
                    if("the_geom".equals(attributeDescriptor.getName().toString())){
                        feature.setGeometry((Geometry)f.getDefaultGeometry());
                    }
                    Object attribute1 = f.getAttribute(attributeDescriptor.getName());
                    attribute.put(attributeDescriptor.getName().toString(),attribute1 == null?null:attribute1.toString());
                }
                feature.setAttribute(attribute);
                current.features().add(feature);
            }

        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
