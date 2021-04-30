package com.alexb.chat.controller;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;
import com.alexb.chat.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient webSocketStompClient;

    @MockBean
    private MessageService messageServiceMock;

    @BeforeEach
    void setUp() {
        webSocketStompClient = new WebSocketStompClient(
                new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    void sendMessage() throws ExecutionException, InterruptedException, TimeoutException {
        UUID roomId = UUID.randomUUID();
        when(messageServiceMock.saveMessage(eq(roomId), any(NewMessageDto.class)))
                .thenReturn(new Message("Alex", roomId, "Alex's message"));

        StompSession session = getSession();
        var stompFrameHandlerHelper = new StompFrameHandlerTestImpl<>(Message.class);
        session.subscribe("/chat/" + roomId + "/messages", stompFrameHandlerHelper);

        NewMessageDto messageToSend = new NewMessageDto("Alex", "Alex's message");
        session.send("/app/chat/" + roomId + "/send-message", messageToSend);

        Message receivedMessage = stompFrameHandlerHelper.getSubscriptionPayload();

        assertNotNull(receivedMessage);
        assertNotEquals(0, receivedMessage.getTimestamp().getEpochSecond());
        assertEquals(roomId, receivedMessage.getRoomId());
        assertEquals(messageToSend.getAuthor(), receivedMessage.getAuthor());
        assertEquals(messageToSend.getContent(), receivedMessage.getContent());
    }

    private StompSession getSession() throws ExecutionException, InterruptedException, TimeoutException {
        return webSocketStompClient.connect(String.format("ws://localhost:%d/alexb-chat", port),
                                            new StompSessionHandlerAdapter() {
                                            })
                                   .get(1, SECONDS);
    }

}
