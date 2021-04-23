package com.alexb.chat.controller;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;
import com.alexb.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;
    private final SimpMessageSendingOperations messagingTemplate;

    @SubscribeMapping("/chat/{roomId}/old-messages")
    public List<Message> getOldChatMessages() {

        return Collections.emptyList();
    }

    @MessageMapping("/chat/{roomId}/send-message")
    public Message sendMessage(@DestinationVariable UUID roomId, NewMessageDto messageDto) {
        Message message = messageService.saveMessage(roomId, messageDto);
        messagingTemplate.convertAndSend(format("/chat/%s/messages", roomId), message);
        return message;
    }

    @MessageExceptionHandler
    @SendTo("/chat/errors")
    public String handleException(Throwable exception) {
        String error = exception.getMessage();
        log.error(error);
        return error;
    }

}
