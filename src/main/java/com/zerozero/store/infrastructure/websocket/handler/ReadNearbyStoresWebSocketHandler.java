package com.zerozero.store.infrastructure.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerozero.store.domain.request.StoreLocationRequest;
import com.zerozero.store.domain.response.StoreResponse;
import com.zerozero.store.domain.service.ReadNearbyStoresUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReadNearbyStoresWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    private final ReadNearbyStoresUseCase readNearbyStoresUseCase;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        StoreLocationRequest storeLocationRequest = objectMapper.readValue(payload, StoreLocationRequest.class);
        List<StoreResponse> stores = readNearbyStoresUseCase.execute(storeLocationRequest);
        if (stores == null) {
            log.error("[ReadNearbyStoresWebSocketHandler] stores is null");
            return;
        }
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(stores)));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("[ReadNearbyStoresWebSocketHandler] Connection established: {}", session.getId());
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("[ReadNearbyStoresWebSocketHandler] Connection closed: {} with userStatus {}", session.getId(), status);
        super.afterConnectionClosed(session, status);
    }

}
