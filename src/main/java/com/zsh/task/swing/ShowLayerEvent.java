package com.zsh.task.swing;

import java.util.EventObject;

public class ShowLayerEvent extends EventObject {
    private boolean message;

    public ShowLayerEvent(Object source, boolean message) {
        super(source);
        this.message = message;
    }

    public boolean getMessage() {
        return message;
    }
}