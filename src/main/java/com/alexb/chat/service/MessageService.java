package com.alexb.chat.service;

import com.alexb.chat.document.Message;
import com.alexb.chat.dto.NewMessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    List<Message> getChatOldMessages(UUID roomId);

    Message saveMessage(UUID roomId, NewMessageDto newMessageDto);

}
