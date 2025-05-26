package com.zsh.task.layer;

import lombok.Data;
import org.locationtech.jts.geom.Geometry;

import java.util.Map;

@Data
public class Feature extends IGeoData {

    private Geometry geometry;

    private Map<String,String> attribute;
}
