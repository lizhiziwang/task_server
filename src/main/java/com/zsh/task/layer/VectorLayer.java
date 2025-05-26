package com.zsh.task.layer;


import cn.hutool.core.util.IdUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  矢量图层接口
 * */
public abstract class VectorLayer implements ILayer{
    private final List<Feature> features = new ArrayList<>();

    private String name;
    private String srid;
    private String type;
    private long id;
    private String path;
    private boolean visible = false;

//    private VectorLayer(){}


    public boolean add(Feature feature) {
        return this.features.add(feature);
    }

    public boolean remove(Feature feature) {
        return this.features.remove(feature);
    }

    @Override
    public List<Feature> features() {
        return features;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getSRID() {
        return this.srid;
    }

    @Override
    public void setSRID(String srid) {
        this.srid = srid;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    @Override
    public void setType(String type){
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId() {
        this.id = IdUtil.getSnowflakeNextId();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}

