package com.example.test_session;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketService webSocketService;
    private final String serviceId;

    public MyWebSocketHandler(WebSocketService webSocketService, String serviceId) {
        this.webSocketService = webSocketService;
        this.serviceId = serviceId;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        webSocketService.sendMessageToInternalServer(serviceId, session, message);
    }
}
