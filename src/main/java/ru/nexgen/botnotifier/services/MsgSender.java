package ru.nexgen.botnotifier.services;

import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
public interface MsgSender {
    void setPlatformSender(Object bot);

    void send(String message, Long chatId);
    void sendWithInlineButtons(Map<String, String> buttons, String headerText, Long chatId);
}
