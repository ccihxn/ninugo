package com.ks.ninugo.service;

import com.ks.ninugo.dto.ChatDTO;
import com.ks.ninugo.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatMapper chatMapper;
    @Autowired
    public ChatService(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }
    public void createChat(ChatDTO chatDTO) {
        chatMapper.createChat(chatDTO);
    }

    public void updateLastChat(int chatId) {
        chatMapper.updateLastChat(chatId);
    }
}
