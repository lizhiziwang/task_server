package com.zsh.task.handler;

import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionSave {
    public final Map<String, Session> map = new ConcurrentHashMap<>();
}
