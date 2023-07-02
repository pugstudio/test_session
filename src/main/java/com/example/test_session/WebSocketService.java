package com.example.test_session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebSocketService {
    private final Map<String, String> uriMap;
    private final WebSocketClient client;
    private final Map<String, WebSocketSession> internalServerSessions;
    private final Cache<String, WebSocketSession> clientSessionMap = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build();

    public WebSocketService() {
        this.uriMap = new HashMap<>();
        this.client = new StandardWebSocketClient();
        this.internalServerSessions = new ConcurrentHashMap<>();

        this.uriMap.put("pepper_core", "ws://localhost:8081/pepper_core");
    }

    @PostConstruct
    public void init() throws Exception {
        // 여기서 serviceId와 URI는 실제 값을 사용해야 합니다.
        String serviceId = "pepper_core";

        for (String s : this.uriMap.keySet()) {
            connectToInternalServer(s);
        }
    }

    private WebSocketSession connectToInternalServer(String serviceId) throws Exception {
        String value = uriMap.get(serviceId);
        URI uri = new URI(value);
        return connectToInternalServer(serviceId, uri);
    }

    private WebSocketSession connectToInternalServer(String serviceId, URI uri) throws Exception {
        WebSocketSession session = client.doHandshake(new InternalServerWebSocketHandler(), String.valueOf(uri)).get();
        internalServerSessions.put(serviceId, session);
        return session;
    }

    public void sendMessageToInternalServer(String serviceId, WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode messageJson = objectMapper.readTree(message.getPayload());
        String url = messageJson.get(0).asText();
        JsonNode header = messageJson.get(1);
        JsonNode body = messageJson.get(2);
        String sessionId = session.getId(); // WebSocket 세션 ID를 사용합니다.
        ((ObjectNode) header).put("sessionId", sessionId);

        if(!clientSessionMap.asMap().containsKey(sessionId)) {
            clientSessionMap.put(sessionId, session);
        }

        WebSocketSession internalServerSession = internalServerSessions.get(serviceId);
        if (internalServerSession == null || !internalServerSession.isOpen()) {
            try {
                internalServerSession = connectToInternalServer(serviceId);
            } catch (Exception e) {
                throw new IllegalStateException("No active WebSocket session for serviceId: " + serviceId);
            }
            internalServerSessions.put(serviceId, internalServerSession);
        }
        internalServerSession.sendMessage(new TextMessage(messageJson.toString()));
    }


    // 내부 서버로부터 받은 메시지를 처리하는 WebSocketHandler
    private class InternalServerWebSocketHandler extends TextWebSocketHandler {
        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
            String payload = message.getPayload();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode messageJson = objectMapper.readTree(payload);
            JsonNode header = messageJson.get(1);

            String sessionId = header.get("sessionId").asText(); // 세션 ID를 추출하는 코드
            WebSocketSession clientSession = clientSessionMap.asMap().get(sessionId);
            if (clientSession != null && clientSession.isOpen()) {
                ((ObjectNode) header).remove("sessionId");
                // 세션이 여전히 열려 있으면, 클라이언트에게 메시지를 보냅니다.
                clientSession.sendMessage(new TextMessage(messageJson.toString()));
            } else {
                // 세션을 찾을 수 없거나 이미 닫혀 있으면, 에러 처리를 합니다.
                // ...
            }

        }
    }
}

