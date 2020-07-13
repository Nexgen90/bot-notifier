package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.MsgSender;

import static ru.nexgen.botnotifier.services.handlers.HelloHandler.HELLO_PREFIX;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HelloCallbackHandler implements MessageHandler {
    private final MsgSender msgSender;

    @Override
    public boolean isValid(Update receivedMessage) {
        if (receivedMessage.hasCallbackQuery() && receivedMessage.getCallbackQuery().getData().startsWith(HELLO_PREFIX)) {
            return true;
        }
        return false;
    }

    @Override
    public void handle(Update receivedMessage) {
        String playerCommand = receivedMessage.getCallbackQuery().getData();
        log.info("Received player callback: " + playerCommand);

        String[] command = playerCommand.split(":");
        int answerId = Integer.parseInt(command[1]);

        switch (answerId) {
            case 1:
                msgSender.send("You peek <b>" + answerId + "</b> value!", receivedMessage.getCallbackQuery().getMessage().getChatId());
                break;
            case 2:
                msgSender.send("You peek <b>" + answerId + "</b> value!", receivedMessage.getCallbackQuery().getMessage().getChatId());
                break;
            case 3:
                msgSender.editInLineButton(receivedMessage, "<i>Отменено</i>");
                break;

        }
    }
}
