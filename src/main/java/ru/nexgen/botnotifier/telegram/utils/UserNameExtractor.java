package ru.nexgen.botnotifier.telegram.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
public class UserNameExtractor {
    public static String extractUserName(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        if (userName == null || userName.isBlank()) {
            userName = update.getMessage().getFrom().getFirstName();
        }
        if (userName == null || userName.isBlank()) {
            userName = update.getMessage().getFrom().getLastName();
        }
        if (userName == null || userName.isBlank()) {
            userName = update.getMessage().getFrom().getId().toString();
        }
        return userName;
    }
}
