package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.MsgSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HelloHandler implements MessageHandler {
    public static final String HELLO_PREFIX = "hello:";
    private final MsgSender msgSender;

    @Override
    public boolean isValid(Update receivedMessage) {
        log.debug("Start HelloHandler isValid check");
        return receivedMessage.hasMessage() && receivedMessage.getMessage().hasText()
                && receivedMessage.getMessage().getText().contains("Hello");
    }

    @Override
    public void handle(Update receivedMessage) {
        log.info("Start HelloHandler handle process");
        if (receivedMessage.getMessage().getText().equalsIgnoreCase("/bot Hello")) {
            msgSender.send("Hello, @" + receivedMessage.getMessage().getFrom().getUserName() + "!", receivedMessage.getMessage().getChatId());
        } else {
            Map<String, String> buttons = new HashMap<>();
            buttons.put("1", HELLO_PREFIX + "1");
            buttons.put("2", HELLO_PREFIX + "2");
            buttons.put("Отмена", HELLO_PREFIX + "3");
            msgSender.sendWithInlineButtons(buttons, "Выбери число:", receivedMessage.getMessage().getChatId()   );
        }
    }
}
