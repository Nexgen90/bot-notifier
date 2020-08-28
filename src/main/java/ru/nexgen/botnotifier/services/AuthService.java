package ru.nexgen.botnotifier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.mapper.Chat;

/**
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final String botName = "ZGameTestBot"; //todo: брать из протертей
    private final DbService dbService;

    public boolean isValid(Update update) {
        Chat chat = createOrGetChat(update);

        if (chat.getBanTime() != null) {
            //todo: проверка даты и времени бана
            return false;
        }

        return  chat.isActive();
    }

    private Chat createOrGetChat(Update update) {
        Chat chat = dbService.getChatsMapper().getChat(update.getMessage().getChatId());
        log.info("Chat from DB: {}", chat);

        if (chat == null) {
            if (update.getMessage().getNewChatMembers() != null
                    && !update.getMessage().getNewChatMembers().isEmpty()) {
                update.getMessage().getNewChatMembers().stream()
                        .filter(member -> botName.equals(member.getUserName()))
                        .findFirst()
                        .ifPresent(m -> dbService.getChatsMapper()
                                .addChat(update.getMessage().getChatId(), update.getMessage().getChat().getTitle()));
            } else {
                String userName = update.getMessage().getFrom().getUserName(); //todo: вынести в отдельный класс
                if (userName == null || userName.isBlank()) {
                    userName = update.getMessage().getFrom().getFirstName();
                }
                if (userName == null || userName.isBlank()) {
                    userName = update.getMessage().getFrom().getLastName();
                }
                if (userName == null || userName.isBlank()) {
                    userName = update.getMessage().getFrom().getId().toString();
                }
                dbService.getChatsMapper()
                        .addChat(update.getMessage().getChatId(), userName);
            }

        }

        if (chat != null && update.getMessage().getLeftChatMember() != null) {
            User leftChatMember = update.getMessage().getLeftChatMember();
            if ( botName.equals(leftChatMember.getUserName()) ) {
                dbService.getChatsMapper().removeChat(update.getMessage().getChatId());
            }
        }

        if (chat != null && update.getMessage().getNewChatMembers() != null
                && !update.getMessage().getNewChatMembers().isEmpty()) {
            update.getMessage().getNewChatMembers().stream()
                    .filter(member -> botName.equals(member.getUserName()))
                    .findFirst()
                    .ifPresent(m -> dbService.getChatsMapper()
                            .updateChatStatus(update.getMessage().getChatId(), true));
        }

        return dbService.getChatsMapper().getChat(update.getMessage().getChatId());
    }
}
