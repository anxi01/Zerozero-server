package com.zerozero.configuration;

import com.zerozero.webSocket.handler.ReadNearbyStoresWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketConfigurer {

  private final ReadNearbyStoresWebSocketHandler readNearbyStoresWebSocketHandler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(readNearbyStoresWebSocketHandler, "/ws/store").setAllowedOrigins("*");
  }
}
