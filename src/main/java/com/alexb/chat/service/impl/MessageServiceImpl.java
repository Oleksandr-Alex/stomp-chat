package com.alexb.chat.service.impl;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;
import com.alexb.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private List<Message> messages = new ArrayList<>();

    @Override
    public List<Message> getChatOldMessages(UUID roomId) {
        return messages;
    }

    @Override
    public Message saveMessage(UUID roomId, NewMessageDto newMessageDto) {
        log.info("Received a new message: {}", newMessageDto.toString());
        Message message = new Message(newMessageDto.getAuthor(), roomId, Instant.now(), newMessageDto.getContent());
        messages.add(message);
        return message;
    }

}
