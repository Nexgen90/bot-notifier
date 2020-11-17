package ru.nexgen.botnotifier.telegram.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

/**
 * Fetch some user name (or id) from incoming update message.
 * Or if can't do it, then return default UNDEFINED_USER name.
 *
 * Created by nikolay.mikutskiy
 * Date: 28.08.2020
 */
@Slf4j
@Component
public class UserNameExtractor {
    private static final String DEFAULT_NAME = "UNDEFINED_USER";

    public String extractUserName(Update update) {
        User user = null;
        if (update.hasMessage()) {
            user = getUser(update.getMessage());
        }

        if (update.hasCallbackQuery()) {
            user = getUser(update.getCallbackQuery());
        }

        if (user == null) {
            log.error("Can't fetch User-object from Update: {}", update);
            return DEFAULT_NAME;
        }

        return getSomeMessageSenderName(user);
    }

    private static User getUser(Message message) {
        return message.getFrom();
    }

    private static User getUser(CallbackQuery callbackQuery) {
        return callbackQuery.getFrom();
    }

    private static String getSomeMessageSenderName(User user) {
        String userName = user.getUserName();

        if (userName == null || userName.isBlank()) {
            userName = user.getFirstName();
        }
        if (userName == null || userName.isBlank()) {
            userName = user.getLastName();
        }
        if (userName == null || userName.isBlank()) {
            userName = user.getId().toString();
        }
        return userName;
    }
}
