package com.king.bishe.chat.mapper;

import com.king.bishe.chat.Pojo.Chat;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author king
 * @date 2020/11/13 16:35
 */
public interface ClientMapper {

    int saveMessage(Chat chat);

    Chat getChat(int id);
}
