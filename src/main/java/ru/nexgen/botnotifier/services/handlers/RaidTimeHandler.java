package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.MsgSender;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by oleg.kurejko
 * Date: 16.07.2020
 */
@Component
@RequiredArgsConstructor
public class RaidTimeHandler implements MessageHandler {
    private final MsgSender msgSender;
    public static final String RECENT_RAIDS = "Ближайшие события:";

    @Override
    public boolean isValid(Update receivedMessage) {
        return receivedMessage.hasMessage() && receivedMessage.getMessage().hasText()
                && receivedMessage.getMessage().getText().startsWith("/raid_time");
    }

    @Override
    public void handle(Update receivedMessage) {
        LocalTime currentTime = LocalTime.now();
        LocalTime raid5 = LocalTime.of(5, 30);
        LocalTime raid8 = LocalTime.of(8, 30);
        LocalTime raid12 = LocalTime.of(12, 30);
        LocalTime raid17 = LocalTime.of(17, 30);
        LocalTime raid23 = LocalTime.of(23, 30);

        String formattedDuration5 = durationFormatting(currentTime, raid5);
        String formattedDuration8 = durationFormatting(currentTime, raid8);
        String formattedDuration12 = durationFormatting(currentTime, raid12);
        String formattedDuration17 = durationFormatting(currentTime, raid17);
        String formattedDuration23 = durationFormatting(currentTime, raid23);

        if (currentTime.isAfter(raid5) && currentTime.isBefore(raid8)) {
            msgSender.send(RECENT_RAIDS + raidTime(raid8) + formattedDuration8 +
                    raidTime(raid12) + formattedDuration12, receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid8) && currentTime.isBefore(raid12)) {
            msgSender.send(RECENT_RAIDS + raidTime(raid12) + formattedDuration12 +
                    raidTime(raid17) + formattedDuration17, receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid12) && currentTime.isBefore(raid17)) {
            msgSender.send(RECENT_RAIDS + raidTime(raid17) + formattedDuration17 +
                    raidTime(raid23) + formattedDuration23, receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid17) && currentTime.isBefore(raid23)) {
            msgSender.send(RECENT_RAIDS + raidTime(raid23) + formattedDuration23 +
                    raidTime(raid5) + formattedDuration5, receivedMessage.getMessage().getChatId());
        }
        if ((currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(raid5)) || (currentTime.isAfter(raid23) && currentTime.isBefore(LocalTime.MIDNIGHT))) {
            msgSender.send(RECENT_RAIDS + raidTime(raid5) + formattedDuration5 +
                    raidTime(raid8) + formattedDuration8, receivedMessage.getMessage().getChatId());
        }
    }


    private static String durationFormatting(LocalTime start, LocalTime end) {
        Duration diff = Duration.between(start, end);
        if (diff.getSeconds() <= 0) {
            diff.plus(24, ChronoUnit.HOURS);
        }
        return String.format("%d:%02d:%02d",
                diff.toHours(),
                diff.toMinutesPart(),
                diff.toSecondsPart());
    }

    private static String raidTime(LocalTime raid) {
        return String.format("\nРейд [%d:%02d] начнётся через: ",
                raid.getHour(),
                raid.getMinute());
    }
}
