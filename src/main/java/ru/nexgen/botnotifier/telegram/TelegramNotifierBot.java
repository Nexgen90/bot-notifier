package ru.nexgen.botnotifier.telegram;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.configuration.properties.TelegramProperties;
import ru.nexgen.botnotifier.services.IncomingTelegramMsgHandler;

import java.util.List;

/**
 * Created by nikolay.mikutskiy
 * Date: 09.07.2020
 */
@Slf4j
@RequiredArgsConstructor
public class TelegramNotifierBot extends TelegramLongPollingBot {
    private final TelegramProperties properties;
    @Setter
    private IncomingTelegramMsgHandler incomingTelegramMsgHandler;

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(this::onUpdateReceived);
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug(update.toString());

        incomingTelegramMsgHandler.handle(update);
    }

    @Override
    public String getBotUsername() {
        return properties.getBotUserName();
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
    }
}
