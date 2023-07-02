package com.example.test_session;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketService webSocketService;
    private List<String> serviceIds;

    public WebSocketConfig(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
        this.serviceIds = new ArrayList<>();
        this.serviceIds.add("pepper_core");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        serviceIds.forEach((serviceId)->{
            registry.addHandler(
                    new MyWebSocketHandler(webSocketService, serviceId), MF.format("/{}", serviceId))
                    .setAllowedOrigins("*");
        });
    }
}
