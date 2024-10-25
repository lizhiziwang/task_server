package com.zsh.task.constant;

public enum MessageType {
    //发送的消息类型，文件，照片，文字，"链接"
    FILE("文件"),
    PIC("照片"),
    TEXT("文字");

    final String type;
    MessageType(String type){
        this.type = type;
    }
}
