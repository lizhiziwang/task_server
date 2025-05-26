package com.zsh.task.layer;


import java.util.List;

/**
 *  图层列表顶层接口
 * */
public interface IMap <T> {
    T getLayer(String name);

    List<T> getAllLayers();

    boolean remove(String name);

    boolean add(T layer);

    boolean addAll(List<T> layers);

    boolean removeAll();
}
