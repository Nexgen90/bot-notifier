package ru.nexgen.botnotifier.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncomingTelegramMsgHandler {
    private final MsgSender msgSender;

    public void handle(Update update) {
        log.info("Update: {}", update);

        if (update.getMessage().getText().equalsIgnoreCase("Hello")) {
            msgSender.send("Hello, " + update.getMessage().getFrom().getUserName() + "!", update.getMessage().getChatId());
        } else {
            Map<String, String> buttons = new HashMap<>();
            buttons.put("1", "1");
            buttons.put("2", "2");
            buttons.put("3", "3");
            msgSender.sendWithInlineButtons(buttons, "Выбери число:", update.getMessage().getChatId()   );
        }

    }
}
