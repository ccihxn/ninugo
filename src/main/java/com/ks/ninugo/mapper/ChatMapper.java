package com.ks.ninugo.mapper;

import com.ks.ninugo.dto.ChatDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {
    void createChat(ChatDTO chatDTO);

    void updateLastChat(int chatId);
}
