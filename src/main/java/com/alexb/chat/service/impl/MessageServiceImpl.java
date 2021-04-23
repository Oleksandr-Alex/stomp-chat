package com.alexb.chat.service.impl;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;
import com.alexb.chat.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    @Override
    public Message saveMessage(UUID roomId, NewMessageDto newMessageDto) {
        log.info("Received a new message: {}", newMessageDto.toString());
        return new Message(newMessageDto.getAuthor(), roomId, Instant.now(), newMessageDto.getContent());
    }

}
