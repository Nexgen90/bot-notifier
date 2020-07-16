package ru.nexgen.botnotifier.services;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationFormat;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalTime;

/**
 * Created by nikolay.mikutskiy
 * Date: 10.07.2020
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IncomingTelegramMsgHandler {
    private final MsgSender msgSender;

    public void handle(Update update) {
        log.info("Update: {}", update);
        String commandCase = "/";
        if (update.getMessage().getText().startsWith(commandCase + "me") || update.getMessage().getText().equalsIgnoreCase("Hello")) {
            if(update.getMessage().getFrom().getLastName() != null && update.getMessage().getFrom().getUserName() != null)
                msgSender.send("Hello, " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName() + "!\nYour username is: @" + update.getMessage().getFrom().getUserName() + "!\nYour id is: " + update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            if(update.getMessage().getFrom().getLastName() != null && update.getMessage().getFrom().getUserName() == null)
                msgSender.send("Hello, " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName() + "!\nYour id is: " + update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            if(update.getMessage().getFrom().getLastName() == null && update.getMessage().getFrom().getUserName() != null)
                msgSender.send("Hello, " + update.getMessage().getFrom().getFirstName() + "!\nYour username is: @" + update.getMessage().getFrom().getUserName() + "!\nYour id is: " + update.getMessage().getFrom().getId(), update.getMessage().getChatId());
            if(update.getMessage().getFrom().getLastName() == null && update.getMessage().getFrom().getUserName() == null)
                msgSender.send("Hello, " + update.getMessage().getFrom().getFirstName() + "!\nYour id is: " + update.getMessage().getFrom().getId(), update.getMessage().getChatId());

        }
        if(update.getMessage().getText().startsWith(("/event_time"))){
            LocalTime currentTime = LocalTime.now();
            LocalTime raid5 = LocalTime.of(5,30);
            LocalTime raid8 = LocalTime.of(8,30);
            LocalTime raid12 = LocalTime.of(12,30);
            LocalTime raid17 = LocalTime.of(17,30);
            LocalTime raid23 = LocalTime.of(23,30);

            String formattedDuration5 = durationFormatting(currentTime,raid5);
            String formattedDuration8 = durationFormatting(currentTime,raid8);
            String formattedDuration12 = durationFormatting(currentTime,raid12);
            String formattedDuration17 = durationFormatting(currentTime,raid17);
            String formattedDuration23 = durationFormatting(currentTime,raid23);

            if(currentTime.isAfter(raid5) && currentTime.isBefore(raid8)) {
                msgSender.send("Ближайшие ивенты:" + "\nРейд [8:30] начнётся через: " + formattedDuration8 +
                        "\nРейд [12:30] начнётся через: " + formattedDuration12, update.getMessage().getChatId());
            }
            if(currentTime.isAfter(raid8) && currentTime.isBefore(raid12)) {
                msgSender.send("Ближайшие ивенты:" + "\nРейд [12:30] начнётся через: " + formattedDuration12 +
                        "\nРейд [17:30] начнётся через: " + formattedDuration17, update.getMessage().getChatId());
            }
            if(currentTime.isAfter(raid12) && currentTime.isBefore(raid17)) {
                msgSender.send("Ближайшие ивенты:" + "\nРейд [17:30] начнётся через: " + formattedDuration17 +
                        "\nРейд [23:30] начнётся через: " + formattedDuration23, update.getMessage().getChatId());
            }
            if(currentTime.isAfter(raid17) && currentTime.isBefore(raid23)) {
                msgSender.send("Ближайшие ивенты:" + "\nРейд [23:30] начнётся через: " + formattedDuration23 +
                        "\nРейд [5:30] начнётся через: " + formattedDuration5, update.getMessage().getChatId());
            }
            if((currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(raid5)) || (currentTime.isAfter(raid23) && currentTime.isBefore(LocalTime.MIDNIGHT))) {
                msgSender.send("Ближайшие ивенты:" + "\nРейд [5:30] начнётся через: " + formattedDuration5 +
                        "\nРейд [8:30] начнётся через: " + formattedDuration8, update.getMessage().getChatId());
            }
        }
    }
    private static String durationFormatting(LocalTime start, LocalTime end)
    {
        Duration diff = Duration.between(start, end);
        if (diff.getSeconds() <= 0) {
            diff.plus(24, ChronoUnit.HOURS);
        }
        return String.format("%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart());
    }
}
