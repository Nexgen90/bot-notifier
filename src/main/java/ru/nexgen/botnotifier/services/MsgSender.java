package ru.nexgen.botnotifier.services;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
public interface MsgSender {
    void setPlatformSender(Object bot);
    void send(String message, Long chatId);
    void sendWithInlineButtons(Map<String, String> buttons, String headerText, Long chatId);
    void editInLineButton(Update update, String text);
}
