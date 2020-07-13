package ru.nexgen.botnotifier.services.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
@Slf4j
@Component
public class AddedNewUserHandler implements MessageHandler {

    @Override
    public boolean isValid(Update receivedMessage) {
        if (receivedMessage.hasMessage()
                && receivedMessage.getMessage().getNewChatMembers() != null
                && receivedMessage.getMessage().getNewChatMembers().size() > 0) {
            return true;
        }

        return false;
    }

    @Override
    public void handle(Update receivedMessage) {
        for (User newChatMember : receivedMessage.getMessage().getNewChatMembers()) {
            if (newChatMember.getBot()) {
                log.info("New bot {} is added in group {}", newChatMember.getId(), receivedMessage.getMessage().getFrom().getId());
                return;
            }
            log.info("New user {} is added in group {}", newChatMember.getId(), receivedMessage.getMessage().getFrom().getId());
        }
    }
}
