package com.zsh.task.layer;

import java.util.List;

public interface ILayer {

//    boolean add(T feature);
//
//    boolean remove(T feature);

    String getType();
    String getSRID();
    List features();

    void setSRID(String srid);
    void setName(String name);
    void setType(String type);
    String getName();
    void setVisible(boolean visible);
    boolean getVisible();
}
