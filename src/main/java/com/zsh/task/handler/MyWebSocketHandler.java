package com.zsh.task.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.zsh.task.config.ThreadPoolConfig;
import com.zsh.task.entity.Message;
import com.zsh.task.service.impl.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MyWebSocketHandler implements WebSocketHandler {
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();

    @Resource
    private MessageServiceImpl ms;
//    @Resource
//    private UserService us;
    @Resource
    private ThreadPoolConfig tpc;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        String query = uri.getQuery();

        SESSIONS.put(query.split("=")[1], session);
    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String user1Id = session.getUri().getQuery().split("=")[1];

        String msg = message.getPayload().toString();
        JSONObject jo = JSONObject.parseObject(msg);
        String user2Id = jo.getString("id");
        //信息持久化
        Runnable task = ()->{
            Message var = new Message();
            var.setContext(jo.getString("message"));
            var.setId(IdUtil.getSnowflakeNextId());
            var.setSendTime(new Date());
            var.setUser1Id(Long.getLong(user1Id));
            var.setUser2Id(Long.getLong(user2Id));
            ms.save(var);
        };
        tpc.poolExecutor().execute(task);
        sendMessage(user2Id,jo.getString("message"));
        System.out.println(msg);
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("连接出错!");
        if (session.isOpen()) {
            session.close();
        }
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("连接已关闭,status:" + closeStatus);
        URI uri = session.getUri();
        String query = uri.getQuery();
        WebSocketSession remove = SESSIONS.remove(query.split("=")[1]);
        System.out.println(remove);
    }
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    /**
     * 指定发消息
     *
     * @param message
     */
    public static void sendMessage(String id, String message) {
        WebSocketSession webSocketSession = SESSIONS.get(id);
        if (webSocketSession == null || !webSocketSession.isOpen())
            return;
        try {
            webSocketSession.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     *
     * @param message
     */
    public static void fanoutMessage(String message) {
        SESSIONS.keySet().forEach(us -> sendMessage(us, message));
    }

//    @PostConstruct
//    public void init() {
//        // 在对象创建后执行的初始化逻辑
//
//        log.info("MessageHandler is Created!");
//        Runnable task = ()->{
//            while (true){
//                if(SESSIONS.size()>0){
//                    Set<String> ids = SESSIONS.keySet();
//                    for(String id:ids){
//                        System.out.println("用户："+id+"zaixian!");
//                    }
//                }
//                else {
//                    log.info("当前没有用户在线！");
//                }
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    log.error(Thread.currentThread().getName()+":"+e.getMessage());
//                }
//            }
//        };
//        tpc.poolExecutor().submit(task);
//    }

//    public static void main(String[] args) {
//        long snowflakeNextId = IdUtil.getSnowflakeNextId();
//        System.out.println(snowflakeNextId);
//    }

}