package com.zerozero.webSocket.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozero.core.application.BaseRequest;
import com.zerozero.core.domain.vo.AccessToken;
import com.zerozero.store.application.ReadNearbyStoresUseCase;
import com.zerozero.store.application.ReadNearbyStoresUseCase.ReadNearbyStoresRequest;
import com.zerozero.store.application.ReadNearbyStoresUseCase.ReadNearbyStoresResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ReadNearbyStoresWebSocketHandler extends TextWebSocketHandler {

  private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private final ReadNearbyStoresUseCase readNearbyStoresUseCase;

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    ReadNearbyStoresWebSocketRequest request = objectMapper.readValue(payload, ReadNearbyStoresWebSocketRequest.class);
    if (request == null || !request.isValid()) {
      log.error("[ReadNearbyStoresWebSocketHandler] request is null");
      return;
    }
    ReadNearbyStoresResponse readNearbyStoresResponse = readNearbyStoresUseCase.execute(
        ReadNearbyStoresRequest.builder()
            .longitude(request.getLongitude())
            .latitude(request.getLatitude())
            .accessToken(request.getAccessToken())
            .build());
    if (readNearbyStoresResponse == null) {
      log.error("[ReadNearbyStoresWebSocketHandler] readNearbyStoresResponse is null");
      return;
    }
    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(readNearbyStoresResponse)));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    log.info("[ReadNearbyStoresWebSocketHandler] Connection established: {}", session.getId());
    super.afterConnectionEstablished(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    log.info("[ReadNearbyStoresWebSocketHandler] Connection closed: {} with status {}", session.getId(), status);
    super.afterConnectionClosed(session, status);
  }

  @ToString
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  @AllArgsConstructor(access = AccessLevel.PRIVATE)
  public static class ReadNearbyStoresWebSocketRequest implements BaseRequest {

    private Double longitude;

    private Double latitude;

    private AccessToken accessToken;

    @Override
    public boolean isValid() {
      return longitude != null && latitude != null && accessToken != null;
    }
  }
}
