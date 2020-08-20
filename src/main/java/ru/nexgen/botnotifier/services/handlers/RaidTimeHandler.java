package ru.nexgen.botnotifier.services.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nexgen.botnotifier.services.MsgSender;
import ru.nexgen.botnotifier.services.TemplatesService;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by oleg.kurejko
 * Date: 16.07.2020
 */
@Component
@RequiredArgsConstructor
public class RaidTimeHandler implements MessageHandler {
    public static final String NEXT_RAID_TIME = "nextRaidTime";
    public static final String NEXT_DURATION = "nextDuration";
    public static final String ANOTHER_RAID_TIME = "anotherRaidTime";
    public static final String ANOTHER_DURATION = "anotherDuration";
    public static final int TEMPLATE_ID = 4;
    private final MsgSender msgSender;
    private final TemplatesService templatesService;

    @Override
    public boolean isValid(Update receivedMessage) {
        return receivedMessage.hasMessage() && receivedMessage.getMessage().hasText()
                && receivedMessage.getMessage().getText().startsWith("/raid_time");
    }

    @Override
    public void handle(Update receivedMessage) {
        Map<String, String> parameters = new HashMap<>();

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
            parameters.put(NEXT_RAID_TIME, raidTime(raid8));
            parameters.put(NEXT_DURATION, formattedDuration8);
            parameters.put(ANOTHER_RAID_TIME, raidTime(raid12));
            parameters.put(ANOTHER_DURATION, formattedDuration12);
            msgSender.send(templatesService.fillTemplate(TEMPLATE_ID, parameters), receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid8) && currentTime.isBefore(raid12)) {
            parameters.put(NEXT_RAID_TIME, raidTime(raid12));
            parameters.put(NEXT_DURATION, formattedDuration12);
            parameters.put(ANOTHER_RAID_TIME, raidTime(raid17));
            parameters.put(ANOTHER_DURATION, formattedDuration17);
            msgSender.send(templatesService.fillTemplate(TEMPLATE_ID, parameters), receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid12) && currentTime.isBefore(raid17)) {
            parameters.put(NEXT_RAID_TIME, raidTime(raid17));
            parameters.put(NEXT_DURATION, formattedDuration17);
            parameters.put(ANOTHER_RAID_TIME, raidTime(raid23));
            parameters.put(ANOTHER_DURATION, formattedDuration23);
            msgSender.send(templatesService.fillTemplate(TEMPLATE_ID, parameters), receivedMessage.getMessage().getChatId());
        }
        if (currentTime.isAfter(raid17) && currentTime.isBefore(raid23)) {
            parameters.put(NEXT_RAID_TIME, raidTime(raid23));
            parameters.put(NEXT_DURATION, formattedDuration23);
            parameters.put(ANOTHER_RAID_TIME, raidTime(raid5));
            parameters.put(ANOTHER_DURATION, formattedDuration5);
            msgSender.send(templatesService.fillTemplate(TEMPLATE_ID, parameters), receivedMessage.getMessage().getChatId());
        }
        if ((currentTime.isAfter(LocalTime.MIDNIGHT) && currentTime.isBefore(raid5))
                || (currentTime.isAfter(raid23) && currentTime.isBefore(LocalTime.MIDNIGHT))) {
            parameters.put(NEXT_RAID_TIME, raidTime(raid5));
            parameters.put(NEXT_DURATION, formattedDuration5);
            parameters.put(ANOTHER_RAID_TIME, raidTime(raid8));
            parameters.put(ANOTHER_DURATION, formattedDuration8);
            msgSender.send(templatesService.fillTemplate(TEMPLATE_ID, parameters), receivedMessage.getMessage().getChatId());
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
        return String.format("%d:%02d", raid.getHour(), raid.getMinute());
    }
}
