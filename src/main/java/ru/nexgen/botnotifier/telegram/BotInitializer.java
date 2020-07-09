package ru.nexgen.botnotifier.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.nexgen.botnotifier.configuration.properties.TelegramProperties;
import ru.nexgen.botnotifier.services.IncomingTelegramMsgHandler;
import ru.nexgen.botnotifier.services.MsgSender;

import javax.annotation.PostConstruct;

/**
 * Created by nikolay.mikutskiy
 * Date: 09.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BotInitializer {
    private final TelegramProperties properties;
    private final MsgSender msgSender;
    private final IncomingTelegramMsgHandler msgHandler;

    @PostConstruct
    public void init() {
        log.info("-------- Try to start bot --------");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        TelegramNotifierBot bot = new TelegramNotifierBot(properties);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            log.error("Can't init bot api: {}", e.getLocalizedMessage());
        }

        msgSender.setPlatformSender(bot);
        bot.setIncomingTelegramMsgHandler(msgHandler);
    }
}
