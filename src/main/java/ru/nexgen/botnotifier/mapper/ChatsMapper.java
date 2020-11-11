package ru.nexgen.botnotifier.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatsMapper {

    void createChatsTableIfNotExists();

    @Update("INSERT INTO chats_id (chat_id, chat_name) VALUES (#{chatId}, #{chatName})")
    void addChat(@Param("chatId") Long chatId,
                 @Param("chatName") String chatName);

    @Select("SELECT chat_id as chatId, chat_name as chatName, start_time as startTime, " +
            "last_call_time as lastCallTime, stop_time as stopTime, ban_time as banTime, " +
            "is_active as isActive " +
            "FROM chats_id WHERE chat_id = #{chatId}")
    Chat getChat(@Param("chatId") Long chatId);

    @Update("UPDATE chats_id SET is_active = false, stop_time = (now() at time zone 'Europe/Moscow') WHERE chat_id = #{chatId}")
    void removeChat(@Param("chatId") Long chatId);

    @Update("UPDATE chats_id SET is_active = #{isActive} WHERE chat_id = #{chatId}")
    void updateChatStatus(@Param("chatId") Long chatId,
                          @Param("isActive") boolean isActive);

    @Update("UPDATE chats_id SET ban_time = (now() at time zone 'Europe/Moscow') + interval '5 minutes' WHERE chat_id = #{chatId}")
    void updateBanTime(@Param("chatId") Long chatId);

    @Select("SELECT chat_id FROM chats_id WHERE is_active = true")
    List<Long> getAllActiveChatIds();

    @Update("UPDATE chats_id SET last_call_time = (now() at time zone 'Europe/Moscow') WHERE chat_id = #{chatId}")
    void updateLastCallTime(@Param("chatId") Long chatId);
}
