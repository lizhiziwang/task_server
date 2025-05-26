package com.zsh.task.swing;

import java.util.EventListener;

public interface  ShowLayerListener extends EventListener {
    void onShowLayer(ShowLayerEvent event);
    void onCloseLayer(ShowLayerEvent event);
}
