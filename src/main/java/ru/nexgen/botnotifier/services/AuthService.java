package ru.nexgen.botnotifier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.configuration.properties.TelegramProperties;
import ru.nexgen.botnotifier.mapper.Chat;
import ru.nexgen.botnotifier.telegram.utils.UserNameExtractor;

import java.time.ZonedDateTime;

/**
 * Manage and track users activity
 *
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final TelegramProperties properties;
    private final DbService dbService;
    private final UserNameExtractor userNameExtractor;

    public boolean isValid(Update update) {
        Chat chat = createOrGetChatFromDB(update);
        dbService.getChatsMapper().updateLastCallTime(chat.getChatId());
        if (isOnBan(chat)) {
            return false;
        }
        return chat.isActive();
    }

    private Chat createOrGetChatFromDB(Update update) {
        Chat chat = dbService.getChatsMapper().getChat(update.getMessage().getChatId());
        log.info("Chat from DB: {}", chat);

        if (chat == null) {
            if (update.getMessage().getNewChatMembers() != null
                    && !update.getMessage().getNewChatMembers().isEmpty()) {
                update.getMessage().getNewChatMembers().stream()
                        .filter(member -> properties.getBotUserName().equals(member.getUserName()))
                        .findFirst()
                        .ifPresent(m -> dbService.getChatsMapper()
                                .addChat(update.getMessage().getChatId(), update.getMessage().getChat().getTitle()));
            } else {
                String userName = userNameExtractor.extractUserName(update);
                dbService.getChatsMapper().addChat(update.getMessage().getChatId(), userName);
            }

        }

        if (chat != null && update.getMessage().getLeftChatMember() != null) {
            User leftChatMember = update.getMessage().getLeftChatMember();
            if ( properties.getBotUserName().equals(leftChatMember.getUserName()) ) {
                dbService.getChatsMapper().setChatToInactive(update.getMessage().getChatId());
            }
        }

        if (chat != null && update.getMessage().getNewChatMembers() != null
                && !update.getMessage().getNewChatMembers().isEmpty()) {
            update.getMessage().getNewChatMembers().stream()
                    .filter(member -> properties.getBotUserName().equals(member.getUserName()))
                    .findFirst()
                    .ifPresent(m -> dbService.getChatsMapper()
                            .updateChatStatus(update.getMessage().getChatId(), true));
        }

        return dbService.getChatsMapper().getChat(update.getMessage().getChatId());
    }

    private boolean isOnBan(Chat chat) {
        if (chat.getBanTime() != null) {
            ZonedDateTime banTime = chat.getBanTime();
            return banTime.isAfter(ZonedDateTime.now(banTime.getZone()));
        }
        return false;
    }
}
