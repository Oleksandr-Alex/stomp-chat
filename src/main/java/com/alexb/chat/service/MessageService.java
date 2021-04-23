package com.alexb.chat.service;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;

import java.util.UUID;

public interface MessageService {

    Message saveMessage(UUID roomId, NewMessageDto newMessageDto);

}
