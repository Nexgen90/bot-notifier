package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.nexgen.botnotifier.services.MsgSender;
import ru.nexgen.botnotifier.services.oapi.GameApiService;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AddedNewUserHandler implements MessageHandler {
    private final GameApiService gameApiService;
    private final MsgSender msgSender;

    @Override
    public boolean isValid(Update receivedMessage) {
        log.debug("Start AddedNewUserHandler isValid check");
        return receivedMessage.hasMessage()
                && receivedMessage.getMessage().getNewChatMembers() != null
                && receivedMessage.getMessage().getNewChatMembers().size() > 0;
    }

    @Override
    public void handle(Update receivedMessage) {
        log.info("Start AddedNewUserHandler handle process");
        for (User newChatMember : receivedMessage.getMessage().getNewChatMembers()) {
            if (newChatMember.getBot()) {
                log.info("New bot {} is added in group {}", newChatMember.getId(), receivedMessage.getMessage().getChatId());
                return;
            }
            log.info("New user {} is added in group {}", newChatMember.getId(), receivedMessage.getMessage().getChatId());

            if (!gameApiService.isGamer(newChatMember.getId())) {
                msgSender.send("Внимание! @" + newChatMember.getUserName() + " не является игроком!", receivedMessage.getMessage().getChatId());
            }
        }
    }
}
