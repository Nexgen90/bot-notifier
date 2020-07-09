package ru.nexgen.botnotifier.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.nexgen.botnotifier.telegram.TelegramNotifierBot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@Service
public class TelegramSender implements MsgSender {
    private TelegramNotifierBot bot;

    @Override
    public void setPlatformSender(Object bot) {
        if (bot instanceof TelegramNotifierBot) {
            this.bot = (TelegramNotifierBot) bot;
        } else {
            throw new RuntimeException("Incompatible sender for Telegram service");
        }
    }

    @Override
    public void send(String message, Long chatId) {
        SendMessage sm = new SendMessage();
        sm.setChatId(chatId);
        sm.enableHtml(true);
        sm.enableWebPagePreview();
        sm.setText(message);
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            log.error("Can't send message: [{}] for {}. Error: {}", message, chatId, e.getLocalizedMessage());
        }
    }

    @Override
    public void sendWithInlineButtons(Map<String, String> buttons, String headerText, Long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> btns = new ArrayList<>();

        buttons.forEach((buttomText, systemText) -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(buttomText);
            button.setCallbackData(systemText);//не видна на экране юзера

            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            buttonsRow.add(button);
            btns.add(buttonsRow);
        });

        keyboard.setKeyboard(btns);
        SendMessage sp = new SendMessage();
        sp.enableHtml(true);
        sp.setChatId(chatId);
        sp.setText(headerText);
        sp.setReplyMarkup(keyboard);

        try {
            Message message = bot.execute(sp);
            log.debug("Sent message = {}", message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }


}
