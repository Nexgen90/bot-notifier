package ru.nexgen.botnotifier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.handlers.MessageHandler;

import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncomingTelegramMsgHandler {
    private final DbService dbService;
    private final AuthService authService;
    private final List<MessageHandler> msgHandlers;

    public void handle(Update update) {
        log.info("Update: {}", update);

        if (!authService.isValid(update)) {
            log.info("Request {} is not valid or blocked", update.getUpdateId());
            return;
        }

        msgHandlers.stream()
                .filter(m -> m.isValid(update))
                .forEach(m -> m.handle(update));
    }
}
