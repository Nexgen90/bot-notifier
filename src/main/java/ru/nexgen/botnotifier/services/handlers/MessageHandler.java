package ru.nexgen.botnotifier.services.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by nikolay.mikutskiy
 * Date: 13.07.2020
 */
public interface MessageHandler {
    boolean isValid(Update receivedMessage);
    void handle(Update receivedMessage);
}
